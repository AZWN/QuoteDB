package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import io.requery.meta.QueryAttribute;
import nl.azwaan.quotedb.models.Author;

public class AuthorsDAO extends BaseDAO<Author> {
    /**
     * Constructs a {@link AuthorsDAO}.
     *
     * @param store The store used to access and manipulate entities.
     */
    @Inject
    public AuthorsDAO(EntityStore<Persistable, Author> store) {
        super(store);
    }

    @Override
    public Class<Author> getEntityClass() {
        return Author.class;
    }

    @Override
    public NumericAttribute<Author, Long> getIDProperty() {
        return Author.ID;
    }

    @Override
    public QueryAttribute<Author, Boolean> getDeletedProperty() {
        return Author.DELETED;
    }
}
