package nl.azwaan.quotedb.api.querybuilding;

import nl.azwaan.quotedb.api.querybuilding.filters.Filter;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;

import java.util.List;

public class BookFilterBuilder extends BaseFilterBuilder<Book> {
    /**
     * Constructs a new basic book filter builder.
     * Filters for owning user, and filters out deleted entities by default.
     *
     * @param dao               The dao object to get property definitions from.
     * @param authenticatedUser The user for which to filter.
     * @param request           The request to build a filter for.
     */
    public BookFilterBuilder(BaseDAO<Book> dao, User authenticatedUser, Request request) {
        super(dao, authenticatedUser, request);
    }

    @Override
    protected List<Filter> getFilters(Request request) {
        final List<Filter> res = super.getFilters(request);

        mapStringLikeFilter(request, res, "title", Book.TITLE);
        mapStringLikeFilter(request, res, "publisher", Book.PUBLISHER);

        return res;
    }
}
