package nl.azwaan.quotedb.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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

import java.util.Optional;

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

    @Override
    protected void resolveReferencesForNewEntity(Book book, User authenticatedUser) {
        super.resolveReferencesForNewEntity(book, authenticatedUser);

        final Author author = authorsDAO.getEntityById(book.getAuthor().getId())
                .orElseThrow(() -> new EntityNotFoundException(Author.class.getName(), book.getAuthor().getId()));
        book.setAuthor(author);
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

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
    private static class BookPatch {
        Optional<String> title = Optional.empty();
        Optional<String> publisher = Optional.empty();
        Optional<Integer> publicationYear = Optional.empty();
        Optional<Long> author = Optional.empty();
    }
}
