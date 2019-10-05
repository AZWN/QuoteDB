package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.Label;

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
    protected Class<Label> getEntityClass() {
        return Label.class;
    }

    /**
     * Creates a new label. Does not check if a label with name labelName already exists.
     *
     * @param labelName The name of the new label.
     * @return The Generated label entity.
     */
    public Label createLabel(String labelName) {
        final Label label = new Label();
        label.setLabelName(labelName);

        store.insert(label);
        store.refresh(label);

        return label;
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
                .limit(1)
                .get()
                .stream()
                .findAny()
                .isPresent();
    }
}
