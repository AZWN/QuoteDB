package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.api.filters.AuthorFilterBuilder;
import nl.azwaan.quotedb.api.filters.BookFilterBuilder;
import nl.azwaan.quotedb.api.filters.EqualityFilter;
import nl.azwaan.quotedb.api.filters.FilterBuilder;
import nl.azwaan.quotedb.api.paging.MultiResultPage;
import nl.azwaan.quotedb.api.paging.PageHelpers;
import nl.azwaan.quotedb.api.patches.AuthorPatch;
import nl.azwaan.quotedb.dao.AuthorsDAO;
import nl.azwaan.quotedb.dao.BooksDAO;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.permissions.PermissionChecker;
import org.jooby.Request;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.Collections;

@Singleton
@Path("/authors")
@Produces("application/json")
@Consumes("application/json")
public class AuthorsAPI extends BaseAPI<Author, AuthorPatch> {

    @Inject BooksDAO booksDAO;
    @Inject PermissionChecker<Book> booksPermissionChecker;

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
        final BookFilterBuilder filterBuilder = new BookFilterBuilder(booksDAO, getAuthenticatedUser(req), req);
        filterBuilder.addFilters(Collections.singletonList(new EqualityFilter<>(Book.AUTHOR_ID, id)));

        return PageHelpers.getPagedResult(req, booksDAO, getAuthenticatedUser(req),
                booksPermissionChecker, filterBuilder);
    }

    @Override
    protected FilterBuilder getDefaultFilterBuilder(Request request) {
        return new AuthorFilterBuilder(dao, getAuthenticatedUser(request), request);
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
