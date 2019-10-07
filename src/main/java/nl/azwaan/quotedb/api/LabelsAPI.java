package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import net.moznion.uribuildertiny.URIBuilderTiny;
import nl.azwaan.quotedb.dao.LabelsDAO;
import nl.azwaan.quotedb.exceptions.ResourceConflictException;
import nl.azwaan.quotedb.models.Label;

import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.HashMap;
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
public class LabelsAPI extends BaseAPI {

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
     * @param request The request for labels.
     * @return A {@link Stream} supplying all available labels.
     */
    @GET
    @Path("")
    public MultiResultPage<Label> getAll(Request request) {
        return getPagedResult(request, labelsDAO, s -> s);
    }


    /**
     * Route to add a new label.
     * @param label The label entity to add.
     * @param req The request to be handled.
     * @return The result of the operation. Either a CREATED response with the resource, or a CONFLICT error.
     */
    @POST
    @Path("")
    public Result addLabel(@Body Label label, Request req) {
        if (labelsDAO.labelExists(label.getLabelName())) {
            throw new ResourceConflictException("Label with name '%s' already exists", label.getLabelName());
        }

        final Label lbl = labelsDAO.createLabel(label.getLabelName());

        final String link = new URIBuilderTiny(req.path())
                .setQueryParameters(new HashMap<>())
                .appendPaths(lbl.getId())
                .build()
                .toString();

        final SingleResultPage<Label> resultPage = new SingleResultPage<>(lbl, link);

        return Results.with(resultPage, Status.CREATED);
    }
}
