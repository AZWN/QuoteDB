package nl.azwaan.quotedb.api.paging;

import io.requery.Persistable;
import nl.azwaan.quotedb.api.filters.FilterBuilder;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.models.UserSpecificModel;
import nl.azwaan.quotedb.permissions.PermissionChecker;
import org.jooby.Request;

import java.util.ArrayList;
import java.util.List;

public final class PageHelpers {
    private PageHelpers() { }

    /**
     * Based on request parameters, performs building paged results nicely.
     * @param request The request that is handled
     * @param dao The data access object to use for querying.
     * @param authenticatedUser The user in whichs context the operation is performed
     * @param permissionChecker Object used to check read permission
     * @param filterBuilder Containing the filters to apply to the base queries
     * @param <TRes> The result data type
     * @return The page with the results
     */
    public static <TRes extends UserSpecificModel & Persistable> MultiResultPage<TRes> getPagedResult(
            Request request, BaseDAO<TRes> dao, User authenticatedUser,
            PermissionChecker<TRes> permissionChecker, FilterBuilder filterBuilder)
    {
        final int totalResults = filterBuilder.buildQuery(dao.countQuery(), false).get().value();

        final int pageSize = filterBuilder.getResultCount();
        final int pageNumber = filterBuilder.getOffset() / filterBuilder.getResultCount();

        final List<TRes> data = new ArrayList<>(pageSize);

        filterBuilder.buildQuery(dao.selectQuery()).get()
                .stream()
                // Check if entity is allowed to be read.
                .peek(elem -> permissionChecker.checkReadEntity(elem, authenticatedUser))
                .forEach(data::add);

        final MultiResultPage<TRes> resultPage =
                MultiResultPage.resultPageFor(data, totalResults, pageSize, pageNumber, request.path());

        return resultPage;
    }
}
