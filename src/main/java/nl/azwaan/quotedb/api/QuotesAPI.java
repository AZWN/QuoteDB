package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.dao.QuotesDAO;
import nl.azwaan.quotedb.models.Quote;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.Optional;
import java.util.stream.Stream;

import static nl.azwaan.quotedb.Constants.MAX_PAGE_SIZE;

/**
 * MVC Controller for manipulating quotes.
 *
 * @author Aron Zwaan
 */
@Singleton
@Path("/quotes")
@Produces("application/json")
@Consumes("application/json")
public class QuotesAPI {

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
     * @param page The page number (default: 1).
     * @param pageSize The page size (default: {@code MAX_PAGE_SIZE}).
     * @return A stream with the selected page of quotes.
     */
    @GET
    @Path("/")
    public Stream<Quote> getAll(Optional<Integer> page, Optional<Integer> pageSize) {
        return quotesDAO.getAllQuotes(page.orElse(1), pageSize.orElse(MAX_PAGE_SIZE));
    }

    /**
     * Creates a new quote in the db.
     *
     * @param quote The quote to create
     * @return The created quote entity
     */
    @POST
    @Path("/")
    public Result insertQuote(Quote quote) {
        final Quote generatedQuote = quotesDAO.createQuote(quote);
        final Result res = Results.ok(generatedQuote);
        res.status(Status.CREATED);
        return res;
    }
}
