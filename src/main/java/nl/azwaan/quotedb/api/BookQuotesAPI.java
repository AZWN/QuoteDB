package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.api.querybuilding.BookQuoteFilterBuilder;
import nl.azwaan.quotedb.api.querybuilding.FilterBuilder;
import nl.azwaan.quotedb.api.patches.BookQuotePatch;
import nl.azwaan.quotedb.dao.BookQuotesDAO;
import nl.azwaan.quotedb.dao.BooksDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.BookQuote;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.permissions.PermissionChecker;
import org.jooby.Request;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

@Singleton
@Path("/bookquotes")
@Produces("application/json")
@Consumes("application/json")
public class BookQuotesAPI extends BaseQuoteAPI<BookQuote, BookQuotePatch> {

    @Inject private BooksDAO booksDAO;
    @Inject protected PermissionChecker<Book> booksPermissionChecker;

    @Inject
    protected BookQuotesAPI(BookQuotesDAO dao) {
        super(dao);
    }

    @Override
    protected void applyPatch(BookQuote quote, User authenticatedUser, BookQuotePatch patch) {
        super.applyPatch(quote, authenticatedUser, patch);
        patch.pageRange.ifPresent(quote::setPageRange);
        patch.book.ifPresent(newBookId -> {
            final Book newBook = booksDAO.getEntityById(newBookId)
                    .orElseThrow(() -> new EntityNotFoundException(Book.class.getName(), newBookId));
            booksPermissionChecker.checkReadEntity(newBook, authenticatedUser);
            quote.setBook(newBook);
        });
    }

    @Override
    protected Class<BookQuotePatch> getPatchClass() {
        return BookQuotePatch.class;
    }

    @Override
    protected FilterBuilder getDefaultFilterBuilder(Request request) {
        return new BookQuoteFilterBuilder(dao, getAuthenticatedUser(request), request);
    }
}
