package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.meta.NumericAttribute;
import io.requery.meta.QueryAttribute;
import nl.azwaan.quotedb.models.Label;
import nl.azwaan.quotedb.models.User;

/**
 * Class defining methods to access labels.
 *
 * @author Aron Zwaan
 */
@Singleton
public class LabelsDAO extends BaseDAO<Label> {

    /**
     * Constructs new {@link LabelsDAO}.
     * @param store The {@link EntityStore} used to access the database.
     */
    @Inject
    public LabelsDAO(EntityStore<Persistable, Label> store) {
        super(store);
    }

    @Override
    public Class<Label> getEntityClass() {
        return Label.class;
    }

    @Override
    public NumericAttribute<Label, Long> getIDProperty() {
        return Label.ID;
    }

    @Override
    public QueryAttribute<Label, Boolean> getDeletedProperty() {
        return Label.DELETED;
    }

    @Override
    public QueryAttribute<Label, User> getUserAttribute() {
        return Label.USER;
    }

    /**
     * Checks if a label already exists.
     *
     * @param labelName The name of the label to check existence for.
     * @return {@code true} if a label with name labelName already exists, {@code false} otherwise.
     */
    public boolean labelExists(String labelName) {
        return store.select(Label.class)
                .where(Label.LABEL_NAME.eq(labelName))
                .and(Label.DELETED.eq(false))
                .limit(1)
                .get()
                .stream()
                .findAny()
                .isPresent();
    }
}
