package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import nl.azwaan.quotedb.models.Book;

import java.util.Optional;

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

    /**
     * Returns a book with the given id, or an empty optional.
     * @param id The id to query for.
     * @return An optional containing the book, or empty if the book was not found.
     */
    public Optional<Book> getBookById(long id) {
        return store.select(Book.class)
                .where(Book.ID.eq(id))
                .get()
                .stream()
                .findAny();
    }
}
