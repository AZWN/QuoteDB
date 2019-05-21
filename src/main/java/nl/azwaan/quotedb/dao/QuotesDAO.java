package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.Quote;

import java.util.stream.Stream;

@Singleton
public class QuotesDAO {

    private EntityStore<Persistable, Quote> quotesEntityStore;

    @Inject
    public QuotesDAO( EntityStore<Persistable, Quote> quotesEntityStore) {
        this.quotesEntityStore = quotesEntityStore;
    }

    public Stream<Quote> getAllQuotes(long categoryId, int page, int pageSize) {
        if (pageSize < 1 || pageSize > 100) {
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

}
