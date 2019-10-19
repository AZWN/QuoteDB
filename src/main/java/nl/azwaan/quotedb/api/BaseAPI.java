package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.query.Return;
import io.requery.query.Scalar;
import io.requery.query.Selection;
import net.moznion.uribuildertiny.URIBuilderTiny;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.dao.UsersDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.models.UserSpecificModel;
import nl.azwaan.quotedb.permissions.PermissionChecker;
import nl.azwaan.quotedb.users.UserIDProvider;
import org.jooby.Request;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.DELETE;
import org.jooby.mvc.GET;
import org.jooby.mvc.PATCH;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static nl.azwaan.quotedb.Constants.MAX_PAGE_SIZE;

@Produces("application/json")
@Consumes("application/json")
public abstract class BaseAPI<T extends UserSpecificModel & Persistable, TPatch> {

    protected BaseDAO<T> dao;
    @Inject protected UsersDAO usersDAO;
    @Inject protected UserIDProvider userIDProvider;
    @Inject protected PermissionChecker<T> permissionChecker;

    protected BaseAPI(BaseDAO<T> dao) {
        this.dao = dao;
    }

    protected MultiResultPage<T> getPagedResult(Request request, BaseDAO<T> dao,
            Function<Selection, Return> queryFilterBuilder)
    {
        final int pageSize = request.param("pageSize").intValue(Constants.MAX_PAGE_SIZE);
        final int pageNumber = request.param("page").intValue(1);

        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize must be between 1 and 100 (inclusive)");
        }

        if (pageNumber < 0) {
            throw new IllegalArgumentException("pageNumber must be 0 or more");
        }

        final int totalResults = ((Scalar<Integer>) queryFilterBuilder.apply(dao.countQuery()).get()).value();

        final List<T> data = new ArrayList<>(pageSize);

        ((Result<T>) queryFilterBuilder.apply(dao.selectQuery()).get())
                .iterator((pageNumber - 1) * pageSize, pageSize)
                .forEachRemaining(elem -> {
                    // Check if entity is allowed to be read.
                    permissionChecker.checkReadEntity(elem, getAuthenticatedUser(request));
                    data.add(elem);
                });

        final MultiResultPage<T> resultPage =
                MultiResultPage.resultPageFor(data, totalResults, pageSize, pageNumber, request.path());

        return resultPage;
    }


    /**
     * Returns all entities for this resource.
     * @param request The request for all entities.
     * @return A page containing all available entities.
     */
    @GET
    @Path("")
    public MultiResultPage<T> getAll(Request request) {
        final User user = getAuthenticatedUser(request);

        return getPagedResult(request, dao, s -> (Return) s.where(dao.getDeletedProperty().eq(false))
                        .and(dao.getUserAttribute().eq(user)));
    }

    /**
     * Route to add a new label.
     * @param req The request to be handled.
     * @return The result of the operation. Either a CREATED response with the resource, or a CONFLICT error.
     *
     * @throws Exception when the request body can not be parsed to the requested entity type.
     */
    @POST
    @Path("")
    public org.jooby.Result addEntity(Request req) throws Exception {
        final User authenticatedUser = getAuthenticatedUser(req);

        final T entity = req.body(dao.getEntityClass());
        resolveReferencesForNewEntity(entity, authenticatedUser);
        permissionChecker.checkCreateEntity(entity, authenticatedUser);
        checkCanInsertEntity(entity);

        final T e = dao.upsertEntity(entity);
        final String link = getEntityURL(req, e.getId());

        final SingleResultPage<T> resultPage = new SingleResultPage<>(e, link);
        return Results.with(resultPage, Status.CREATED);
    }

    /**
     * Instead of adding new entities for subfields, resolve existing entities.
     * @param entity The entity to resolve reference from.
     * @param authenticatedUser The user performing these operations.
     */
    protected void resolveReferencesForNewEntity(T entity, User authenticatedUser) {
        entity.setUserOnSubfields(authenticatedUser);
    }

    protected User getAuthenticatedUser(Request req) {
        final Long authenticatedUserId = userIDProvider.getUserId(req);
        return usersDAO.getUserById(authenticatedUserId);
    }

    protected String getEntityURL(Request req, Object... e) {
        return new URIBuilderTiny(req.path())
                    .setQueryParameters(new HashMap<>())
                    .appendPaths(e)
                    .build()
                    .toString();
    }

    protected void checkCanInsertEntity(T entity) { }

    /**
     * Gets a single entity.
     * @param req The request that is to be handled.
     * @param id The id of the entity as part of the URL.
     *
     * @return A page with the found entity as result.
     */
    @GET
    @Path("/:id")
    public SingleResultPage<T> getSingleEntity(Request req, Long id) {
        final T entity = dao.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(dao.getEntityClass().getName(), id));

        final User user = getAuthenticatedUser(req);
        permissionChecker.checkReadEntity(entity, user);

        return new SingleResultPage<T>(entity, getEntityURL(req));
    }

    /**
     * Deletes a single entity.
     * @param req The request that is served.
     * @param id The id of the entity as part of the URL.
     *
     * @return The appropriate status code (200/404)
     */
    @DELETE
    @Path("/:id")
    public org.jooby.Result deleteEntity(Request req, Long id) {
        final Optional<T> entity = dao.getEntityById(id);
        entity.map(x -> {
            permissionChecker.checkDeleteEntity(x, getAuthenticatedUser(req));
            return x;
        });

        return (dao.deleteEntityById(id)) ? Results.ok() : Results.with(Status.NOT_FOUND);
    }

    /**
     * Updates an existing quote.
     * @param request The request that is handled
     * @param id The id of the quote to be patched
     * @throws Exception When the body cannot be parsed to the requested path type
     *
     * @return A page containing the updated resource.
     */
    @PATCH
    @Path("/:id")
    public SingleResultPage<T> updateEntity(Request request, Long id) throws Exception {
        final TPatch patch = request.body(getPatchClass());
        final User authenticatedUser = getAuthenticatedUser(request);
        final T entity = dao.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(dao.getEntityClass().getName(), id));

        permissionChecker.checkUpdateEntity(entity, authenticatedUser);
        applyPatch(entity, authenticatedUser, patch);
        dao.updateEntity(entity);

        return new SingleResultPage<>(entity, getEntityURL(request));
    }

    protected abstract void applyPatch(T entity, User authenticatedUser, TPatch patch);

    protected abstract Class<TPatch> getPatchClass();
}
