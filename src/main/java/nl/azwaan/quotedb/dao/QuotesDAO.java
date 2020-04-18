package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import io.requery.meta.QueryAttribute;
import nl.azwaan.quotedb.models.QuickQuote;
import nl.azwaan.quotedb.models.User;

import java.util.Date;

/**
 * Object to access quotes from the database.
 *
 * @author Aron Zwaan
 */
@Singleton
public class QuotesDAO extends BaseQuotesDAO<QuickQuote> {

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

    @Override
    public QueryAttribute<QuickQuote, User> getUserAttribute() {
        return QuickQuote.USER;
    }

    @Override
    public NumericAttribute<QuickQuote, Date> getLastModifiedDateProperty() {
        return QuickQuote.LAST_MODIFIED_DATE;
    }

    @Override
    public NumericAttribute<QuickQuote, Date> getGenerationDateProperty() {
        return QuickQuote.GENERATION_DATE;
    }
}
