package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.azwaan.quotedb.dao.LabelsDAO;
import nl.azwaan.quotedb.models.Label;

import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.stream.Stream;

/**
 * MVC Controller for manipulating {@link Label} resources.
 *
 * @author Aron Zwaan
 */
@Singleton
@Path("/labels")
@Produces("application/json")
@Consumes("application/json")
public class LabelsAPI {

    private LabelsDAO labelsDAO;

    /**
     * Constructs a new controller.
     * @param labelsDAO The object used to access the database.
     */
    @Inject
    public LabelsAPI(LabelsDAO labelsDAO) {
        this.labelsDAO = labelsDAO;
    }

    /**
     * Returns all labels.
     * @return A {@link Stream} supplying all available labels.
     */
    @GET
    @Path("")
    public Stream<Label> getAll() {
        return labelsDAO.getLabels();
    }


    /**
     * Route to add a new label.
     * @param label The label entity to add.
     * @return The result of the operation. Either a CREATED response with the resource, or a CONFLICT error.
     */
    @POST
    @Path("")
    public Result addLabel(@Body Label label) {
        if (labelsDAO.labelExists(label.labelName)) {
            throw new ResourceConflictException("Label with name '%s' already exists", label.labelName);
        }

        final Label lbl = labelsDAO.createLabel(label.labelName);
        final Result res = Results.ok(lbl);
        res.status(Status.CREATED);
        return res;
    }
}
