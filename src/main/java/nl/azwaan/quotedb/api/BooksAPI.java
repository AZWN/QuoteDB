package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.dao.BaseDAO;
import nl.azwaan.quotedb.models.Book;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

@Singleton
@Path("/books")
@Produces("application/json")
@Consumes("application/json")
public class BooksAPI extends BaseAPI<Book> {

    @Inject
    protected BooksAPI(BaseDAO<Book> dao) {
        super(dao);
    }
}
