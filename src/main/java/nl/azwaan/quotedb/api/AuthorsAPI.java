package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.dao.AuthorsDAO;
import nl.azwaan.quotedb.models.Author;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

@Singleton
@Path("/authors")
@Produces("application/json")
@Consumes("application/json")
public class AuthorsAPI extends BaseAPI<Author> {

    @Inject
    protected AuthorsAPI(AuthorsDAO dao) {
        super(dao);
    }
}
