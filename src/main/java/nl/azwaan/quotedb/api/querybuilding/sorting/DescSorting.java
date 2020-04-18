package nl.azwaan.quotedb.api.querybuilding.sorting;

import io.requery.query.Functional;
import io.requery.query.OrderingExpression;

public class DescSorting implements Sorting {

    private Functional<?> attribute;

    /**
     * Constructs a new descending sorting clause.
     * @param attribute the attribute to sort on.
     */
    public DescSorting(Functional<?> attribute) {
        this.attribute = attribute;
    }

    @Override
    public OrderingExpression<?> create() {
        return attribute.desc();
    }
}
