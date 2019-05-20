package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.dao.LabelsDAO;
import nl.azwaan.quotedb.models.Label;
import org.jooby.Response;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Body;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;

import java.util.stream.Stream;

@Singleton
@Path("/labels")
public class LabelsAPI {

    private LabelsDAO labelsDAO;

    @Inject
    public LabelsAPI(LabelsDAO labelsDAO) {
        this.labelsDAO = labelsDAO;
    }

    @GET
    @Path("")
    public Stream<Label> getAll() {
        return labelsDAO.getLabels();
    }

    @POST
    @Path("")
    public Result addLabel(@Body PostLabel label) {
        if (labelsDAO.labelExists(label.labelName)) {
            Result res = new Result();
            res.status(Status.CONFLICT);
            res.set(new APIError("This label already exists"));
            return res;
        }

        Label lbl = labelsDAO.createLabel(label.labelName);
        Result res = Results.ok(lbl);
        res.status(Status.CREATED);
        return res;
    }

    public static class PostLabel {
        public String labelName;
    }
}
