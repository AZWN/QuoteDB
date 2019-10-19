package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import io.requery.Persistable;
import nl.azwaan.quotedb.api.patches.QuotePatch;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.dao.LabelsDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.BaseQuote;
import nl.azwaan.quotedb.models.Label;
import nl.azwaan.quotedb.models.QuickQuote;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.permissions.PermissionChecker;
import org.jooby.Request;
import org.jooby.mvc.Body;
import org.jooby.mvc.PATCH;
import org.jooby.mvc.Path;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class BaseQuoteAPI<TQuote extends BaseQuote & Persistable,
        TPatch extends QuotePatch> extends BaseAPI<TQuote>
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


    /**
     * Updates an existing quote.
     * @param request The request that is handled
     * @param id The id of the quote to be patched
     * @param patch The patch to be applied
     *
     * @return A page containing the updated resource.
     */
    @PATCH
    @Path("/:id")
    public SingleResultPage<TQuote> patchQuote(Request request, Long id, @Body TPatch patch) {
        final User authenticatedUser = getAuthenticatedUser(request);
        final TQuote quote = dao.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(QuickQuote.class.getName(), id));

        permissionChecker.checkUpdateEntity(quote, authenticatedUser);
        applyPatch(quote, authenticatedUser, patch);
        dao.updateEntity(quote);

        return new SingleResultPage<>(quote, getEntityURL(request));
    }
}
