package nl.azwaan.quotedb.api.querybuilding.sorting;

import io.requery.query.Functional;
import io.requery.query.OrderingExpression;

public class AscSorting implements Sorting {

    private Functional<?> attribute;

    /**
     * Constructs a new ascending sorting clause.
     * @param attribute the attribute to sort on.
     */
    public AscSorting(Functional<?> attribute) {
        this.attribute = attribute;
    }

    @Override
    public OrderingExpression<?> create() {
        return attribute.asc();
    }
}
