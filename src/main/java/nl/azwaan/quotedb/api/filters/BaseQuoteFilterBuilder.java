package nl.azwaan.quotedb.api.filters;

import io.requery.Persistable;
import io.requery.meta.StringAttribute;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.BaseQuote;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;

import java.util.List;

public abstract class BaseQuoteFilterBuilder<TQuote extends BaseQuote & Persistable> extends BaseFilterBuilder<TQuote> {
    /**
     * Constructs a new basic quote filter builder.
     * Filters for owning user, and filters out deleted entities by default.
     * Additionally allows filtering by title and text.
     *
     * @param dao               The dao object to get property definitions from.
     * @param authenticatedUser The user for which to filter.
     * @param request           The request to build a filter for.
     */
    public BaseQuoteFilterBuilder(BaseDAO<TQuote> dao, User authenticatedUser, Request request) {
        super(dao, authenticatedUser, request);
    }

    @Override
    protected List<Filter> getFilters(Request request) {
        final List<Filter> res = super.getFilters(request);

        mapStringLikeFilter(request, res, "title", getTitleAttribute());
        mapStringLikeFilter(request, res, "text", getTextAttribute());

        return res;
    }

    protected abstract StringAttribute<TQuote, String> getTextAttribute();

    protected abstract StringAttribute<TQuote, String> getTitleAttribute();
}
