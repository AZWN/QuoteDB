package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.api.patches.AuthorPatch;
import nl.azwaan.quotedb.dao.AuthorsDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.ArrayList;

@Singleton
@Path("/authors")
@Produces("application/json")
@Consumes("application/json")
public class AuthorsAPI extends BaseAPI<Author, AuthorPatch> {

    @Inject
    protected AuthorsAPI(AuthorsDAO dao) {
        super(dao);
    }

    /**
     * Gets all books of a specific author.
     * @param req The request to be served
     * @param id The author id.
     * @return A page with the books of the author.
     */
    @GET
    @Path("/:id/books")
    public MultiResultPage<Book> getAuthorBooks(Request req, Long id) {
        final User authenticatedUser = getAuthenticatedUser(req);
        final Author author = dao.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(Author.class.getName(), id));
        permissionChecker.checkReadEntity(author, authenticatedUser);

        final int bookCount = author.getBooks().size();

        return MultiResultPage.resultPageFor(new ArrayList<>(author.getBooks()),
                bookCount, Constants.MAX_PAGE_SIZE, 1, getEntityURL(req));
    }

    @Override
    protected void applyPatch(Author author, User authenticatedUser, AuthorPatch patch) {
        patch.firstName.ifPresent(author::setFirstName);
        patch.lastName.ifPresent(author::setLastName);
        patch.middleName.ifPresent(author::setMiddleName);
        patch.initials.ifPresent(author::setInitials);
        patch.dateOfBirth.ifPresent(author::setDateOfBirth);
        patch.biography.ifPresent(author::setBiography);
    }

    @Override
    protected Class<AuthorPatch> getPatchClass() {
        return AuthorPatch.class;
    }
}
