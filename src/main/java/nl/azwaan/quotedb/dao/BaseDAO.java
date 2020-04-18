package nl.azwaan.quotedb.dao;

import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import io.requery.meta.QueryAttribute;
import io.requery.query.Functional;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.requery.query.Selection;
import io.requery.query.Update;
import nl.azwaan.quotedb.models.User;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class BaseDAO<T extends Persistable> {

    protected EntityStore<Persistable, T> store;

    /**
     * Constructs a {@link BaseDAO}.
     * @param store The store used to access and manipulate entities.
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
     * Returns a basic update query. This can be used to build more sophisticated update queries on.
     * @return A basic update query.
     */
    public Update<? extends Scalar<Integer>> updateQuery() {
        return store.update(getEntityClass());
    }

    /**
     * Returns a basic select query. This can be used to build more sophisticated select queries on.
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
                .where(getDeletedProperty().eq(false))
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

    /**
     * Upserts an entity into the database.
     * @param entity The entity to be upserted.
     * @return The upserted entity.
     */
    public T upsertEntity(T entity) {
        store.upsert(store.refresh(entity));
        return store.refresh(entity);
    }

    /**
     * @return The id {@link io.requery.meta.Attribute} of the entity type T.
     */
    public abstract NumericAttribute<T, Long> getIDProperty();

    /**
     * @return The delete {@link io.requery.meta.Attribute} of the entity type T.
     */
    public abstract QueryAttribute<T, Boolean> getDeletedProperty();

    /**
     * @return The user {@link io.requery.meta.Attribute} of the entity type T.
     */
    public abstract QueryAttribute<T, User> getUserAttribute();

    /**
     * Returns an entity with the given id.
     * @param id The id of the entity to be queried
     * @return An Optional containing either the found entity, or empty when the entity was not found.
     */
    public Optional<T> getEntityById(Long id) {
        try (Stream<T> s = selectQuery()
                .where(getIDProperty().eq(id))
                .and(getDeletedProperty().eq(false))
                .get()
                .stream())
        {
            return s.findAny();
        }
    }

    /**
     * Deletes an entity with the given id.
     * @param id The id of the entity to delete.
     *
     * @return true if any record got deleted, false otherwise.
     */
    public boolean deleteEntityById(Long id) {
        return updateQuery()
                .set(getDeletedProperty(), true)
                .where(getIDProperty().eq(id))
                .and(getDeletedProperty().eq(false))
                .get()
                .value() > 0;
    }

    /**
     * Updates a modified entity.
     * @param entity The entity to be updated.
     * @return The updated entity.
     */
    public T updateEntity(T entity) {
        return store.update(entity);
    }

    /**
     * Returns the property when entity is modified last.
     * @return The LastModifiedDate attribute
     */
    public abstract NumericAttribute<T, Date> getLastModifiedDateProperty();

    /**
     * Returns the property when entity is created.
     * @return The GenerationDate attribute
     */
    public abstract NumericAttribute<T, Date> getGenerationDateProperty();
}
