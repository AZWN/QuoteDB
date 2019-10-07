package nl.azwaan.quotedb.api;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.requery.query.Selection;
import net.moznion.uribuildertiny.URIBuilderTiny;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.BaseModel;
import org.jooby.Request;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static nl.azwaan.quotedb.Constants.MAX_PAGE_SIZE;

@Produces("application/json")
@Consumes("application/json")
public abstract class BaseAPI<T extends BaseModel & Persistable> {

    private BaseDAO<T> dao;

    protected BaseAPI(BaseDAO<T> dao) {
        this.dao = dao;
    }

    protected MultiResultPage<T> getPagedResult(Request request, BaseDAO<T> dao,
            Function<Selection, Selection> queryFilterBuilder)
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
                .forEachRemaining(data::add);

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
        return getPagedResult(request, dao, s -> s);
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
        final T entity = req.body(dao.getEntityClass());
        checkCanInsertEntity(entity);

        final T e = dao.insertEntity(entity);

        final String link = getEntityURL(req, e.getId());

        final SingleResultPage<T> resultPage = new SingleResultPage<>(e, link);

        return Results.with(resultPage, Status.CREATED);
    }

    private String getEntityURL(Request req, Object... e) {
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

        return new SingleResultPage<T>(entity, getEntityURL(req));
    }
}
