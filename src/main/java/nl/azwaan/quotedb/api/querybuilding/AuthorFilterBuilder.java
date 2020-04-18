package nl.azwaan.quotedb.api.querybuilding;

import nl.azwaan.quotedb.api.querybuilding.filters.Filter;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;

import java.util.List;

public class AuthorFilterBuilder extends BaseFilterBuilder<Author> {
    /**
     * Constructs a new basic author filter builder.
     * Filters for owning user, and filters out deleted entities by default.
     * Also adds filters for first and last name, if provided.
     *
     * @param dao               The dao object to get property definitions from.
     * @param authenticatedUser The user for which to filter.
     * @param request The request to obtain filters from.
     */
    public AuthorFilterBuilder(BaseDAO<Author> dao, User authenticatedUser, Request request) {
        super(dao, authenticatedUser, request);
    }

    @Override
    protected List<Filter> getFilters(Request request) {
        final List<Filter> res = super.getFilters(request);

        mapStringLikeFilter(request, res, "firstName", Author.FIRST_NAME);
        mapStringLikeFilter(request, res, "lastName", Author.LAST_NAME);

        return res;
    }
}
