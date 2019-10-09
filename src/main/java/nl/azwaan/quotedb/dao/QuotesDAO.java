package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import io.requery.meta.QueryAttribute;
import nl.azwaan.quotedb.models.QuickQuote;

/**
 * Object to access quotes from the database.
 *
 * @author Aron Zwaan
 */
@Singleton
public class QuotesDAO extends BaseDAO<QuickQuote> {

    /**
     * Creates object to query Quotes.
     *
     * @param store Requery {@link EntityStore} to access quotes.
     */
    @Inject
    public QuotesDAO(EntityStore<Persistable, QuickQuote> store) {
        super(store);
    }

    @Override
    public Class<QuickQuote> getEntityClass() {
        return QuickQuote.class;
    }

    @Override
    public NumericAttribute<QuickQuote, Long> getIDProperty() {
        return QuickQuote.ID;
    }

    @Override
    public QueryAttribute<QuickQuote, Boolean> getDeletedProperty() {
        return QuickQuote.DELETED;
    }

}
