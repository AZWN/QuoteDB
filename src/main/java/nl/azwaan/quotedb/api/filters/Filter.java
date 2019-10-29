package nl.azwaan.quotedb.api.filters;

import io.requery.query.WhereAndOr;

public interface Filter {
    /**
     * Applies the filter to a query.
     * @param query The query to apply to
     * @param <T> The return type of the query
     * @return The new query.
     */
    <T> WhereAndOr<T> apply(WhereAndOr<T> query);
}
