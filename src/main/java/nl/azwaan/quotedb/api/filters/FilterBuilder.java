package nl.azwaan.quotedb.api.filters;

import io.requery.query.Return;
import io.requery.query.Selection;

public interface FilterBuilder {
    /**
     * Function building the filter over the query.
     * @param <T> the result Type of the query
     * @param baseQuery The unfiltered query.
     * @param includePaging If true, the filter builder should add offset and limit constraints.
     * @return The filtered query.
     */
    <T> Return<T> buildQuery(Selection<T> baseQuery, boolean includePaging);

    /**
     * Default function building the filter over the query. Will include paging.
     * @param <T> the result Type of the query
     * @param baseQuery The unfiltered query.
     * @return The filtered query with paging.
     */
    default <T> Return<T> buildQuery(Selection<T> baseQuery) {
        return buildQuery(baseQuery, true);
    }

    /**
     * Returns the size of the result set of this query. Returns (-1) if no page size.
     * @return The result set size
     */
    int getResultCount();

    /**
     * Returns the offset of the query (the skipped results). Defaults to 0.
     * @return The query offset
     */
    int getOffset();
}
