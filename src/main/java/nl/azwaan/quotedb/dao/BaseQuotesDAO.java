package nl.azwaan.quotedb.dao;

import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.BaseQuote;
import nl.azwaan.quotedb.models.Label;

import java.util.Set;

public abstract class BaseQuotesDAO<TQuote extends BaseQuote & Persistable> extends BaseDAO<TQuote> {

    /**
     * Constructs a {@link BaseQuotesDAO}.
     *
     * @param store The store used to access and manipulate entities.
     */
    public BaseQuotesDAO(EntityStore<Persistable, TQuote> store) {
        super(store);
    }

    @Override
    public TQuote upsertEntity(TQuote quote) {
        final Set<Label> labels = quote.getLabels();
        final TQuote upsertedQuote = super.upsertEntity(quote);
        // Add labels after upserting, since requery bug.
        upsertedQuote.getLabels().clear();
        upsertedQuote.getLabels().addAll(labels);
        return updateEntity(upsertedQuote);
    }
}
