package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import nl.azwaan.quotedb.models.QuickQuote;

import java.util.stream.Stream;

import static nl.azwaan.quotedb.Constants.MAX_PAGE_SIZE;

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

    /**
     * Returns all quotes.
     *
     * @param page The page number
     * @param pageSize The page size
     * @return The quotes in the selected category and page.
     */
    public Stream<QuickQuote> getAllQuotes(int page, int pageSize) {
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize must be between 1 and 100 (inclusive)");
        }

        return store.select(QuickQuote.class)
                .orderBy(QuickQuote.GENERATION_DATE.asc(), QuickQuote.ID.asc())
                .limit(page * pageSize)
                .get()
                .stream()
                .skip((page - 1) * pageSize);
    }

}
