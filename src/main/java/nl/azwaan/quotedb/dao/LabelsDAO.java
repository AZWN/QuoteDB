package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.Label;

import java.util.stream.Stream;

@Singleton
public class LabelsDAO {

    private EntityStore<Persistable, Label> labelEntityStore;

    @Inject
    public LabelsDAO(EntityStore<Persistable, Label> labelEntityStore) {
        this.labelEntityStore = labelEntityStore;
    }

    public Stream<Label> getLabels() {
        return labelEntityStore.select(Label.class)
                .get()
                .stream();
    }

    public Label createLabel(String labelName) {
        Label label = new Label();
        label.labelName = labelName;

        labelEntityStore.insert(label);
        // labelEntityStore.refresh(label);

        return label;
    }

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
