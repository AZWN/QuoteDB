package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.api.patches.BookQuotePatch;
import nl.azwaan.quotedb.dao.BookQuotesDAO;
import nl.azwaan.quotedb.dao.BooksDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.BookQuote;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.permissions.PermissionChecker;
import org.jooby.Request;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.PATCH;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

@Singleton
@Path("/bookquotes")
@Produces("application/json")
@Consumes("application/json")
public class BookQuotesAPI extends BaseAPI<BookQuote> {

    @Inject private BooksDAO booksDAO;
    @Inject protected PermissionChecker<Book> booksPermissionChecker;

    @Inject
    protected BookQuotesAPI(BookQuotesDAO dao) {
        super(dao);
    }

    /**
     * Updates an existing book quote.
     * @param request he request that is handled.
     * @param id The id of the quote to be updated.
     * @param patch The path to be applied.
     * @return A page containing the updated resource.
     */
    @PATCH
    @Path("/:id")
    public SingleResultPage<BookQuote> updateBookQuote(Request request, Long id, @Body BookQuotePatch patch) {
        final User authenticatedUser = getAuthenticatedUser(request);
        final BookQuote quote = dao.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(BookQuote.class.getName(), id));

        permissionChecker.checkUpdateEntity(quote, authenticatedUser);

        patch.note.ifPresent(quote::setNote);
        patch.text.ifPresent(quote::setText);
        patch.title.ifPresent(quote::setTitle);
        patch.book.ifPresent(newBookId -> {
            final Book newBook = booksDAO.getEntityById(newBookId)
                    .orElseThrow(() -> new EntityNotFoundException(Book.class.getName(), newBookId));
            booksPermissionChecker.checkReadEntity(newBook, authenticatedUser);
            quote.setBook(newBook);
        });
        patch.pageRange.ifPresent(quote::setPageRange);

        dao.updateEntity(quote);
        return new SingleResultPage<>(quote, getEntityURL(request));
    }
}
