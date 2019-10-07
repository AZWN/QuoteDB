package nl.azwaan.quotedb.dao;

import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.Author;

public class AuthorsDAO extends BaseDAO<Author> {
    /**
     * Constructs a {@link AuthorsDAO}.
     *
     * @param store The store used to access and manipulate entities.
     */
    public AuthorsDAO(EntityStore<Persistable, Author> store) {
        super(store);
    }

    @Override
    public Class<Author> getEntityClass() {
        return Author.class;
    }
}
