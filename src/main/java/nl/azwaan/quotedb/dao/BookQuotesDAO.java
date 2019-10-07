package nl.azwaan.quotedb.dao;

import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.BookQuote;

/**
 * Classed used for data operations on {@link BookQuote}s.
 *
 * @author Aron Zwaan
 */
public class BookQuotesDAO extends BaseDAO<BookQuote> {
    /**
     * Constructs a {@link BookQuote}.
     *
     * @param store The store used to access and manipulate entities.
     */
    public BookQuotesDAO(EntityStore<Persistable, BookQuote> store) {
        super(store);
    }

    @Override
    public Class<BookQuote> getEntityClass() {
        return BookQuote.class;
    }
}
