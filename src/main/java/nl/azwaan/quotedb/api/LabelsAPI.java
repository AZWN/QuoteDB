package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.azwaan.quotedb.api.filters.FilterBuilder;
import nl.azwaan.quotedb.api.filters.LabelsFilterBuilder;
import nl.azwaan.quotedb.api.patches.LabelPatch;
import nl.azwaan.quotedb.dao.LabelsDAO;
import nl.azwaan.quotedb.exceptions.ResourceConflictException;
import nl.azwaan.quotedb.models.Label;

import nl.azwaan.quotedb.models.User;
import org.jooby.Request;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

/**
 * MVC Controller for manipulating {@link Label} resources.
 *
 * @author Aron Zwaan
 */
@Singleton
@Path("/labels")
@Produces("application/json")
@Consumes("application/json")
public class LabelsAPI extends BaseAPI<Label, LabelPatch> {

    private LabelsDAO labelsDAO;

    /**
     * Constructs a new controller.
     * @param labelsDAO The object used to access the database.
     */
    @Inject
    public LabelsAPI(LabelsDAO labelsDAO) {
        super(labelsDAO);
        this.labelsDAO = labelsDAO;
    }

    @Override
    protected void checkCanInsertEntity(@Body Label label) {
        if (labelsDAO.labelExists(label.getLabelName())) {
            throw new ResourceConflictException("Label with name '%s' already exists", label.getLabelName());
        }
    }

    @Override
    protected void applyPatch(Label label, User authenticatedUser, LabelPatch patch) {
        patch.color.ifPresent(label::setColor);
        patch.labelName.ifPresent(label::setLabelName);
    }

    @Override
    protected Class<LabelPatch> getPatchClass() {
        return LabelPatch.class;
    }

    @Override
    protected FilterBuilder getDefaultFilterBuilder(Request request) {
        return new LabelsFilterBuilder(dao, getAuthenticatedUser(request), request);
    }
}
