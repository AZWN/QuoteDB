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

@Singleton
@Path("/categories/:categoryId/quotes")
@Produces("application/json")
@Consumes(value = {"application/json"})
public class QuotesAPI {

    private QuotesDAO quotesDAO;

    @Inject
    public QuotesAPI(QuotesDAO quotesDAO) {
        this.quotesDAO = quotesDAO;
    }

    @GET
    @Path("/")
    public Stream<Quote> getAll(long categoryId, Optional<Integer> page, Optional<Integer> pageSize) {
        return quotesDAO.getAllQuotes(categoryId, page.orElse(1), pageSize.orElse(100));
    }
}
