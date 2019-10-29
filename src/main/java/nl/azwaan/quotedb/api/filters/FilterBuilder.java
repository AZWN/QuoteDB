package nl.azwaan.quotedb.api.filters;

import io.requery.query.Return;
import io.requery.query.Selection;
import io.requery.query.WhereAndOr;

public interface FilterBuilder {
    /**
     * Function building the filter over the query.
     * @param <T> the result Type of the query
     * @param baseQuery The unfiltered query.
     * @return The filtered query.
     */
    <T> WhereAndOr<T> addFilter(Selection<T> baseQuery);

    /**
     * Function finalizing the query (adding paging and limits etc).
     * @param <T> The return type fo the query
     * @param filteredQuery The query with filters
     * @return The finalized query
     */
    <T> Return<T> finalizeQuery(WhereAndOr<T> filteredQuery);
}
