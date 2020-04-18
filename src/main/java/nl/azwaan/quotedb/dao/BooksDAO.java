package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import io.requery.meta.QueryAttribute;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.User;

import java.util.Date;

@Singleton
public class BooksDAO extends BaseDAO<Book> {

    /**
     * Constructs new BooksDAO with the given entitystore.
     * @param store The store to query books to.
     */
    @Inject
    public BooksDAO(EntityStore<Persistable, Book> store) {
        super(store);
    }

    @Override
    public Class<Book> getEntityClass() {
        return Book.class;
    }

    @Override
    public NumericAttribute<Book, Long> getIDProperty() {
        return Book.ID;
    }

    @Override
    public QueryAttribute<Book, Boolean> getDeletedProperty() {
        return Book.DELETED;
    }

    @Override
    public QueryAttribute<Book, User> getUserAttribute() {
        return Book.USER;
    }

    @Override
    public NumericAttribute<Book, Date> getLastModifiedDateProperty() {
        return Book.LAST_MODIFIED_DATE;
    }

    @Override
    public NumericAttribute<Book, Date> getGenerationDateProperty() {
        return Book.GENERATION_DATE;
    }

}
