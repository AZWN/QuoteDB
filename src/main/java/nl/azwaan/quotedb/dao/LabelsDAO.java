package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.Label;

import java.util.stream.Stream;

/**
 * Class defining methods to access labels.
 *
 * @author Aron Zwaan
 */
@Singleton
public class LabelsDAO {

    private EntityStore<Persistable, Label> labelEntityStore;

    /**
     * Constructs new {@link LabelsDAO}.
     * @param labelEntityStore The {@link EntityStore} used to access the database.
     */
    @Inject
    public LabelsDAO(EntityStore<Persistable, Label> labelEntityStore) {
        this.labelEntityStore = labelEntityStore;
    }

    /**
     * Returns all labels.
     * @return A {@link Stream} supplying all labels in the database.
     */
    public Stream<Label> getLabels() {
        return labelEntityStore.select(Label.class)
                .get()
                .stream();
    }

    /**
     * Creates a new label. Does not check if a label with name labelName already exists.
     *
     * @param labelName The name of the new label.
     * @return The Generated label entity.
     */
    public Label createLabel(String labelName) {
        final Label label = new Label();
        label.labelName = labelName;

        labelEntityStore.insert(label);
        labelEntityStore.refresh(label);

        return label;
    }

    /**
     * Checks if a label already exists.
     *
     * @param labelName The name of the label to check existence for.
     * @return {@code true} if a label with name labelName already exists, {@code false} otherwise.
     */
    public boolean labelExists(String labelName) {
        return labelEntityStore.select(Label.class)
                .where(Label.LABEL_NAME.eq(labelName))
                .limit(1)
                .get()
                .stream()
                .findAny()
                .isPresent();
    }
}
