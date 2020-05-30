package nl.azwaan.quotedb.api.querybuilding;

import io.requery.Persistable;
import io.requery.meta.StringAttribute;
import io.requery.query.Expression;
import io.requery.query.Functional;
import io.requery.query.Limit;
import io.requery.query.LogicalCondition;
import io.requery.query.OrderingExpression;
import io.requery.query.Return;
import io.requery.query.Selection;
import io.requery.query.WhereAndOr;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.api.querybuilding.filters.EqualityFilter;
import nl.azwaan.quotedb.api.querybuilding.filters.Filter;
import nl.azwaan.quotedb.api.querybuilding.filters.StringLikeFilter;
import nl.azwaan.quotedb.api.querybuilding.sorting.AscSorting;
import nl.azwaan.quotedb.api.querybuilding.sorting.DescSorting;
import nl.azwaan.quotedb.api.querybuilding.sorting.Sorting;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.exceptions.InvalidSortingSpecificationException;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.models.UserSpecificModel;
import org.jooby.Mutant;
import org.jooby.Request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static nl.azwaan.quotedb.Constants.MAX_PAGE_SIZE;

public class BaseFilterBuilder<T extends UserSpecificModel & Persistable> implements FilterBuilder {

    private final int resultSetSize;
    private final int offset;
    private BaseDAO<T> dao;

    private LogicalCondition<? extends Expression<User>, ?> initialFilter;
    private List<Filter> filters;

    private List<Sorting> sortings;

    /**
     * Constructs a new basic filter builder.
     * Filters for owning user, and filters out deleted entities by default.
     * @param dao The dao object to get property definitions from.
     * @param authenticatedUser The user for which to filter.
     * @param request The request to build a filter for.
     */
    public BaseFilterBuilder(BaseDAO<T> dao, User authenticatedUser, Request request) {
        this.dao = dao;
        resultSetSize = request.param("pageSize").intValue(Constants.MAX_PAGE_SIZE);
        offset = (request.param("page").intValue(1) - 1) * resultSetSize;

        if (resultSetSize < 1 || resultSetSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize must be between 1 and 100 (inclusive)");
        }

        if (offset < 0) {
            throw new IllegalArgumentException("pageNumber must be 1 or more");
        }

        initialFilter = dao.getUserAttribute().eq(authenticatedUser);
        filters = getFilters(request);

        sortings = getSortings(request);
    }

    protected List<Sorting> getSortings(Request request) {
        final List<Sorting> result = new ArrayList<>();
        mapSorting(request, result, "last_modified_date", dao.getLastModifiedDateProperty());
        mapSorting(request, result, "generation_date", dao.getGenerationDateProperty());
        mapSorting(request, result, "id", dao.getIDProperty());

        return result;
    }

    protected List<Filter> getFilters(Request request) {
        final List<Filter> result = new ArrayList<>();

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

    /**
     * Adds where filter to query.
     * @param s The base query
     * @param <R> The query result type
     * @return The query with where clause
     */
    public <R> WhereAndOr<R> addFilter(Selection<R> s) {
        WhereAndOr<R> res = s.where(initialFilter);

        for (Filter filter : filters) {
            res = filter.apply(res);
        }

        return res;
    }

    @Override
    public <R> Return<R> buildQuery(Selection<R> baseQuery, boolean includePaging) {
        final WhereAndOr<R> intermediate = addFilter(baseQuery);
        if (includePaging) {
            return finalizeQuery(intermediate);
        }
        return intermediate;
    }

    protected <R> Return<R> finalizeQuery(WhereAndOr<R> filteredQuery) {
        final Limit<R> preProcessedQuery = preFinalize(filteredQuery);
        return preProcessedQuery.limit(resultSetSize).offset(offset);
    }

    protected <R> Limit<R> preFinalize(WhereAndOr<R> filteredQuery) {
        final OrderingExpression[] sortingExpressions = sortings.stream()
                .map(Sorting::create).toArray(OrderingExpression[]::new);
        return filteredQuery.orderBy(sortingExpressions);
    }

    protected void mapStringLikeFilter(Request request, List<Filter> filters, String paramName,
                                       StringAttribute<T, String> attribute)
    {
        request.param(paramName).toOptional().ifPresent(value -> filters.add(new StringLikeFilter<>(attribute, value)));
    }

    protected void mapSorting(Request request, List<Sorting> sortings, String attributeName, Functional<?> attribute) {

        request.param("order_by").toList().stream()
                .filter(x -> x.startsWith(attributeName))
                .findFirst()
                .ifPresent(value -> {
                    if (value.equals(String.format("%s.desc", attributeName))) {
                        sortings.add(new DescSorting(attribute));
                    }
                    else if (value.equals(String.format("%s.asc", attributeName))) {
                        sortings.add(new AscSorting(attribute));
                    }
                    else if (value.equals(attributeName)) {
                        sortings.add(new AscSorting(attribute));
                    }
                    else {
                        throw new InvalidSortingSpecificationException(value);
                    }
                });
    }

    /**
     * Adds custom filters/conditions.
     * @param filters The filters to add.
     */
    public void addFilters(Collection<Filter> filters) {
        this.filters.addAll(filters);
    }

    public int getResultCount() {
        return resultSetSize;
    }

    public int getOffset() {
        return offset;
    }
}
