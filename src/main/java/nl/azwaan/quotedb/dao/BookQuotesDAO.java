package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import io.requery.meta.QueryAttribute;
import nl.azwaan.quotedb.models.BookQuote;
import nl.azwaan.quotedb.models.User;

import java.util.Date;

/**
 * Classed used for data operations on {@link BookQuote}s.
 *
 * @author Aron Zwaan
 */
@Singleton
public class BookQuotesDAO extends BaseQuotesDAO<BookQuote> {
    /**
     * Constructs a {@link BookQuote}.
     *
     * @param store The store used to access and manipulate entities.
     */
    @Inject
    public BookQuotesDAO(EntityStore<Persistable, BookQuote> store) {
        super(store);
    }

    @Override
    public Class<BookQuote> getEntityClass() {
        return BookQuote.class;
    }

    @Override
    public NumericAttribute<BookQuote, Long> getIDProperty() {
        return BookQuote.ID;
    }

    @Override
    public QueryAttribute<BookQuote, Boolean> getDeletedProperty() {
        return BookQuote.DELETED;
    }

    @Override
    public QueryAttribute<BookQuote, User> getUserAttribute() {
        return BookQuote.USER;
    }

    @Override
    public NumericAttribute<BookQuote, Date> getLastModifiedDateProperty() {
        return BookQuote.LAST_MODIFIED_DATE;
    }

    @Override
    public NumericAttribute<BookQuote, Date> getGenerationDateProperty() {
        return BookQuote.GENERATION_DATE;
    }
}
