package nl.azwaan.quotedb.api.filters;

import io.requery.meta.StringAttribute;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.QuickQuote;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;

public class QuickQuoteFilter extends BaseQuoteFilter<QuickQuote> {
    /**
     * Constructs a new basic quick quote filter builder.
     * Filters for owning user, and filters out deleted entities by default.
     * Additionally allows filtering by title and text.
     *
     * @param dao               The dao object to get property definitions from.
     * @param authenticatedUser The user for which to filter.
     * @param request           The request to build a filter for.
     */
    public QuickQuoteFilter(BaseDAO<QuickQuote> dao, User authenticatedUser, Request request) {
        super(dao, authenticatedUser, request);
    }

    @Override
    protected StringAttribute<QuickQuote, String> getTextAttribute() {
        return QuickQuote.TEXT;
    }

    @Override
    protected StringAttribute<QuickQuote, String> getTitleAttribute() {
        return QuickQuote.TITLE;
    }
}
