package nl.azwaan.quotedb.api.querybuilding.sorting;

import io.requery.query.OrderingExpression;

/**
 * Interface for collecting sort clauses.
 *
 * @author Aron Zwaan
 */
public interface Sorting {

    /**
     * Create sorting clause.
     * @return The expression to order on.
     */
    OrderingExpression<?> create();
}
