package nl.azwaan.quotedb.api.querybuilding.filters;

import io.requery.query.Conditional;
import io.requery.query.Expression;
import io.requery.query.LogicalCondition;
import io.requery.query.WhereAndOr;

public class EqualityFilter<TAttr> implements Filter {

    private Conditional<LogicalCondition<? extends Expression<TAttr>, ?>, TAttr> attribute;

    private TAttr value;

    /**
     * Creates a new filter requiering the attribute to be equalt to the provided value.
     * @param attribute The attribute to compare
     * @param value The value it should compare equal to.
     */
    public EqualityFilter(Conditional<LogicalCondition<? extends Expression<TAttr>, ?>, TAttr> attribute, TAttr value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public <T> WhereAndOr<T> apply(WhereAndOr<T> query) {
        return query.and(attribute.eq(value));
    }
}
