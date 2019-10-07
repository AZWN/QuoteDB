package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import nl.azwaan.quotedb.models.BookQuote;

/**
 * Classed used for data operations on {@link BookQuote}s.
 *
 * @author Aron Zwaan
 */
@Singleton
public class BookQuotesDAO extends BaseDAO<BookQuote> {
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
}
