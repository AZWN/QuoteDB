package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.Quote;

import java.util.stream.Stream;

import static nl.azwaan.quotedb.Constants.MAX_PAGE_SIZE;

/**
 * Object to access quotes from the database.
 *
 * @author Aron Zwaan
 */
@Singleton
public class QuotesDAO {

    private EntityStore<Persistable, Quote> quotesEntityStore;

    /**
     * Creates object to query Quotes.
     *
     * @param quotesEntityStore Requery {@link EntityStore} to access quotes.
     */
    @Inject
    public QuotesDAO(EntityStore<Persistable, Quote> quotesEntityStore) {
        this.quotesEntityStore = quotesEntityStore;
    }

    /**
     * Returnes all quotes in a category.
     *
     * @param categoryId The category to select quotes from.
     * @param page The page number
     * @param pageSize The page size
     * @return The quotes in the selected category and page.
     */
    public Stream<Quote> getAllQuotes(long categoryId, int page, int pageSize) {
        if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize must be between 1 and 100 (inclusive)");
        }

        return quotesEntityStore.select(Quote.class)
                .where(Quote.CATEGORY_ID.eq(categoryId))
                .orderBy(Quote.GENERATION_DATE.asc(), Quote.ID.asc())
                .limit(page * pageSize)
                .get()
                .stream()
                .skip((page - 1) * pageSize);
    }

    /**
     * Creates a new quote.
     *
     * @param quote The quote to create
     * @return The created quote
     */
    public Quote createQuote(Quote quote) {
        quotesEntityStore.insert(quote);
        quotesEntityStore.refresh(quote);
        return quote;
    }

}
