package nl.azwaan.quotedb.dao;

import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.requery.query.Selection;

import java.util.stream.Stream;

public abstract class BaseDAO<T extends Persistable> {

    protected EntityStore<Persistable, T> store;

    /**
     * constructs a {@link BaseDAO}.
     * @param store The sotre used to access and manipulate entities.
     */
    public BaseDAO(EntityStore<Persistable, T> store) {
        this.store = store;
    }

    /**
     * Returns a basic count query. This can be used to build more sophisticated count queries on.
     * @return A basic count query.
     */
    public Selection<? extends Scalar<Integer>> countQuery() {
        return store.count(getEntityClass());
    }


    /**
     * Returns a basic count query. This can be used to build more sophisticated select queries on.
     * @return A basic select query.
     */
    public Selection<? extends Result<T>> selectQuery() {
        return store.select(getEntityClass());
    }

    /**
     * Returns a runtime {@link Class} value for T. Used t build base queries with.
     * @return a runtime {@link Class} value for T
     */
    public abstract Class<T> getEntityClass();

    /**
     * Returns all entities of this type in the database.
     * @return A Stream containing all the entities.
     */
    public Stream<T> getAll() {
        return selectQuery()
                .get()
                .stream();
    }

    /**
     * Inserts a new entity into the database.
     * @param entity The entity to be inserted.
     * @return The inserted entity.
     */
    public T insertEntity(T entity) {
        store.insert(entity);
        return store.refresh(entity);
    }
}
