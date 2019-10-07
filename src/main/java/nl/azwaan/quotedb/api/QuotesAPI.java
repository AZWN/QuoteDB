package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.moznion.uribuildertiny.URIBuilderTiny;
import nl.azwaan.quotedb.dao.QuotesDAO;
import nl.azwaan.quotedb.models.QuickQuote;
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

/**
 * MVC Controller for manipulating quotes.
 *
 * @author Aron Zwaan
 */
@Singleton
@Path("/quotes")
@Produces("application/json")
@Consumes("application/json")
public class QuotesAPI extends BaseAPI {

    private QuotesDAO quotesDAO;

    /**
     * Creates a new {@link QuotesAPI} controller.
     * @param quotesDAO The db accessor for quotes.
     */
    @Inject
    public QuotesAPI(QuotesDAO quotesDAO) {
        this.quotesDAO = quotesDAO;
    }

    /**
     * Returns all quotes in a category.
     * @param req The request to be served.
     * @return A stream with the selected page of quotes.
     */
    @GET
    @Path("/")
    public MultiResultPage<QuickQuote> getAll(Request req) {
        return getPagedResult(req, quotesDAO, s -> s);
    }

    /**
     * Creates a new quote in the db.
     *
     * @param quote The quote to create
     * @param req The request to be handled
     * @return The created quote entity
     */
    @POST
    @Path("/")
    public Result insertQuote(@Body QuickQuote quote, Request req) {
        final QuickQuote generatedQuote = quotesDAO.createQuote(quote);

        final String link = new URIBuilderTiny(req.path())
                .setQueryParameters(new HashMap<>())
                .appendPaths(generatedQuote.getId())
                .build()
                .toString();

        final SingleResultPage<QuickQuote> resultPage = new SingleResultPage<>(generatedQuote, link);

        return Results.with(resultPage, Status.CREATED);
    }
}
