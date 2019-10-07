package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.dao.BookQuotesDAO;
import nl.azwaan.quotedb.models.BookQuote;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

@Singleton
@Path("/bookquotes")
@Produces("application/json")
@Consumes("application/json")
public class BookQuotesAPI extends BaseAPI<BookQuote> {

    @Inject
    protected BookQuotesAPI(BookQuotesDAO dao) {
        super(dao);
    }
}
