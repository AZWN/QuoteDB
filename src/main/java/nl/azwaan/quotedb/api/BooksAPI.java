package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.api.patches.BookPatch;
import nl.azwaan.quotedb.dao.AuthorsDAO;
import nl.azwaan.quotedb.dao.BooksDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.PATCH;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

@Singleton
@Path("/books")
@Produces("application/json")
@Consumes("application/json")
public class BooksAPI extends BaseAPI<Book> {

    @Inject private AuthorsDAO authorsDAO;

    @Inject
    protected BooksAPI(BooksDAO dao) {
        super(dao);
    }

    /**
     * Updates a book resource.
     * @param request The request to be served
     * @param id The id of the book to be updated.
     * @param patch The properties to be updated.
     *
     * @return A page containing the updated resource.
     */
    @PATCH
    @Path("/:id")
    public SingleResultPage<Book> updateBook(Request request, Long id, @Body BookPatch patch) {
        final User authenticatedUser = getAuthenticatedUser(request);
        final Book book = dao.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(Book.class.getName(), id));

        permissionChecker.checkUpdateEntity(book, authenticatedUser);

        patch.title.ifPresent(book::setTitle);
        patch.publisher.ifPresent(book::setPublisher);
        patch.publicationYear.ifPresent(book::setPublicationYear);
        patch.author.ifPresent(authorId -> {
            // TODO: Permissions
            final Author author = authorsDAO.getEntityById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException(Author.class.getName(), authorId));
            book.setAuthor(author);
        });

        dao.updateEntity(book);
        return new SingleResultPage<>(book, getEntityURL(request));
    }

    @Override
    protected void resolveReferencesForNewEntity(Book entity, User authenticatedUser) {
        super.resolveReferencesForNewEntity(entity, authenticatedUser);

        // If author already in db, replace with real entity
        authorsDAO.getEntityById(entity.getAuthor().getId())
                .ifPresent(entity::setAuthor);
    }

}
