package nl.azwaan.quotedb.api.filters;

import io.requery.Persistable;
import io.requery.meta.StringAttribute;
import io.requery.query.Expression;
import io.requery.query.LogicalCondition;
import io.requery.query.Return;
import io.requery.query.Selection;
import io.requery.query.WhereAndOr;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.models.UserSpecificModel;
import org.jooby.Mutant;
import org.jooby.Request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseFilterBuilder<T extends UserSpecificModel & Persistable> implements FilterBuilder {

    private BaseDAO<T> dao;
    private User authenticatedUser;

    private LogicalCondition<? extends Expression<Long>, ?> initialFilter;
    private List<Filter> filters;

    /**
     * Constructs a new basic filter builder.
     * Filters for owning user, and filters out deleted entities by default.
     * @param dao The dao object to get property definitions from.
     * @param authenticatedUser The user for which to filter.
     * @param request The request to build a filter for.
     */
    public BaseFilterBuilder(BaseDAO<T> dao, User authenticatedUser, Request request) {
        this.dao = dao;
        this.authenticatedUser = authenticatedUser;

        initialFilter = dao.getIDProperty().greaterThanOrEqual(-1L);
        filters = getFilters(request);
    }

    protected List<Filter> getFilters(Request request) {
        final List<Filter> result = new ArrayList<>();

        result.add(new EqualityFilter<>(dao.getUserAttribute(), authenticatedUser));

        // Find out whether to include deleted properties
        final Mutant includeDeletedProp = request.param("includeDeleted");
        boolean includeDeleted = false;
        if (includeDeletedProp.isSet()) {
            final String val = includeDeletedProp.toOptional().orElse("false");
            if (!val.toLowerCase().equals("false")) {
                includeDeleted = true;
            }
        }

        if (!includeDeleted) {
            result.add(new EqualityFilter<>(dao.getDeletedProperty(), false));
        }

        return result;
    }

    @Override
    public <R> WhereAndOr<R> addFilter(Selection<R> s) {
        WhereAndOr<R> res = s.where(initialFilter);

        for (Filter filter : filters) {
            res = filter.apply(res);
        }

        return res;
    }

    @Override
    public <R> Return<R> finalizeQuery(WhereAndOr<R> filteredQuery) {
        return filteredQuery;
    }

    protected void mapStringLikeFilter(Request request, List<Filter> filters, String paramName,
                                       StringAttribute<T, String> attribute)
    {
        request.param(paramName).toOptional().ifPresent(value -> filters.add(new StringLikeFilter<>(attribute, value)));
    }

    /**
     * Adds custom filters/conditions.
     * @param filters The filters to add.
     */
    public void addFilters(Collection<Filter> filters) {
        this.filters.addAll(filters);
    }
}
