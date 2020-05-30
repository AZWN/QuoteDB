package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import io.requery.Persistable;
import nl.azwaan.quotedb.api.patches.QuotePatch;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.dao.LabelsDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.BaseQuote;
import nl.azwaan.quotedb.models.Label;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.permissions.PermissionChecker;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseQuoteAPI<TQuote extends BaseQuote & Persistable,
        TPatch extends QuotePatch> extends BaseAPI<TQuote, TPatch>
{

    @Inject protected PermissionChecker<Label> labelPermissionChecker;
    @Inject protected LabelsDAO labelsDAO;

    protected BaseQuoteAPI(BaseDAO<TQuote> dao) {
        super(dao);
    }

    protected void applyPatch(TQuote quote, User authenticatedUser, TPatch patch) {
        patch.note.ifPresent(quote::setNote);
        patch.text.ifPresent(quote::setText);
        patch.title.ifPresent(quote::setTitle);
        patch.addLabels.ifPresent(lblIds -> {
            final Collection<Label> addedLabels = lblIds.stream()
                    .map(lblId -> labelsDAO.getEntityById(lblId)
                            .orElseThrow(() -> new EntityNotFoundException(Label.class.getName(), lblId)))
                    .peek(lbl -> labelPermissionChecker.checkReadEntity(lbl, authenticatedUser))
                    .collect(Collectors.toList());
            quote.getLabels().addAll(addedLabels);
        });
        patch.removeLabels.ifPresent(lblIds -> {
            final Collection<Label> removedLabels = lblIds.stream()
                    .map(lblId -> labelsDAO.getEntityById(lblId)
                            .orElseThrow(() -> new EntityNotFoundException(Label.class.getName(), lblId)))
                    .peek(lbl -> labelPermissionChecker.checkReadEntity(lbl, authenticatedUser))
                    .collect(Collectors.toList());
            quote.getLabels().removeAll(removedLabels);
        });
    }

    @Override
    protected void resolveReferencesForNewEntity(TQuote entity, User authenticatedUser) {
        super.resolveReferencesForNewEntity(entity, authenticatedUser);

        // Due to the fact that composed keys are not expressable when using @Superclass
        // We need to manually map the labels to the right entities
        try (Stream<Label> labelsStream = entity.getLabels().stream()) {
            final Set<Label> labels = labelsStream
                    .map(lbl -> {
                        if (labelsDAO.labelExists(lbl.getLabelName(), authenticatedUser)) {
                            return labelsDAO.getLabelByNameAndUser(lbl.getLabelName(), authenticatedUser);
                        }
                        lbl.setUser(authenticatedUser);
                        return labelsDAO.insertEntity(lbl);
                    })
                    .collect(Collectors.toSet());

            entity.getLabels().clear();
            entity.getLabels().addAll(labels);
        }
    }
}
