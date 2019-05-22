package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.dao.QuotesDAO;
import nl.azwaan.quotedb.models.Quote;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
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
@Path("/categories/:categoryId/quotes")
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
     * @param categoryId The id of the category to search in.
     * @param page The page number (default: 1).
     * @param pageSize The page size (default: {@code MAX_PAGE_SIZE}).
     * @return A stream with the selected page of quotes.
     */
    @GET
    @Path("/")
    public Stream<Quote> getAll(long categoryId, Optional<Integer> page, Optional<Integer> pageSize) {
        return quotesDAO.getAllQuotes(categoryId, page.orElse(1), pageSize.orElse(MAX_PAGE_SIZE));
    }
}
