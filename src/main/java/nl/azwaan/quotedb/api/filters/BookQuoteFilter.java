package nl.azwaan.quotedb.api.filters;

import io.requery.meta.StringAttribute;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.BookQuote;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;

public class BookQuoteFilter extends BaseQuoteFilter<BookQuote> {
    /**
     * Constructs a new basic book quote filter builder.
     * Filters for owning user, and filters out deleted entities by default.
     *
     * @param dao               The dao object to get property definitions from.
     * @param authenticatedUser The user for which to filter.
     * @param request           The request to build a filter for.
     */
    public BookQuoteFilter(BaseDAO<BookQuote> dao, User authenticatedUser, Request request) {
        super(dao, authenticatedUser, request);
    }

    @Override
    protected StringAttribute<BookQuote, String> getTextAttribute() {
        return BookQuote.TEXT;
    }

    @Override
    protected StringAttribute<BookQuote, String> getTitleAttribute() {
        return BookQuote.TITLE;
    }
}
