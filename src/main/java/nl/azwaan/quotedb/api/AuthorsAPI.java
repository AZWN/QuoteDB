package nl.azwaan.quotedb.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.dao.AuthorsDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.PATCH;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Singleton
@Path("/authors")
@Produces("application/json")
@Consumes("application/json")
public class AuthorsAPI extends BaseAPI<Author> {

    @Inject
    protected AuthorsAPI(AuthorsDAO dao) {
        super(dao);
    }

    /**
     * Handler for requests to update {@link Author} resources.
     * Can update any editable property, except the books reference.
     * This property should be edited through the /api/books endpoint.
     *
     * @param req The request that is served
     * @param id The id of the author to update
     * @param patch The properties to be updated.
     *
     * @return A page containing the updated resource.
     */
    @PATCH
    @Path("/:id")
    public SingleResultPage updateAuthor(Request req, Long id, @Body AuthorPatch patch) {
        final User authenticatedUser = getAuthenticatedUser(req);
        final Author author = dao.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(Author.class.getName(), id));

        permissionChecker.checkUpdateEntity(author, authenticatedUser);

        patch.firstName.ifPresent(author::setFirstName);
        patch.lastName.ifPresent(author::setLastName);
        patch.middleName.ifPresent(author::setMiddleName);
        patch.initials.ifPresent(author::setInitials);
        patch.dateOfBirth.ifPresent(author::setDateOfBirth);
        patch.biography.ifPresent(author::setBiography);

        dao.updateEntity(author);
        return new SingleResultPage<>(author, getEntityURL(req));
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

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
    private static class AuthorPatch {
        Optional<String> firstName = Optional.empty();
        Optional<String> lastName = Optional.empty();
        Optional<String> middleName = Optional.empty();
        Optional<String> initials = Optional.empty();
        Optional<Date> dateOfBirth = Optional.empty();
        Optional<String> biography = Optional.empty();
    }
}
