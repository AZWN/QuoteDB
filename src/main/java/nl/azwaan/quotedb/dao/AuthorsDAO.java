package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import io.requery.meta.QueryAttribute;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.User;

import java.util.Date;

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

    @Override
    public QueryAttribute<Author, User> getUserAttribute() {
        return Author.USER;
    }

    @Override
    public NumericAttribute<Author, Date> getLastModifiedDateProperty() {
        return Author.LAST_MODIFIED_DATE;
    }

    @Override
    public NumericAttribute<Author, Date> getGenerationDateProperty() {
        return Author.GENERATION_DATE;
    }
}
