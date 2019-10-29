package nl.azwaan.quotedb.api.filters;

import io.requery.meta.StringAttribute;
import io.requery.query.WhereAndOr;

public class StringLikeFilter<TEntity> implements Filter {

    private StringAttribute<TEntity, String> attribute;

    private String value;

    /**
     * Creates a string filter for an attribute.
     * @param attribute The attribute to be filtered
     * @param value The value to filter on
     */
    public StringLikeFilter(StringAttribute<TEntity, String> attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public <T> WhereAndOr<T> apply(WhereAndOr<T> query) {
        return query.and(attribute.like(value));
    }
}
