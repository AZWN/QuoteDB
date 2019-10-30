package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.api.filters.BookFilterBuilder;
import nl.azwaan.quotedb.api.filters.BookQuoteFilterBuilder;
import nl.azwaan.quotedb.api.filters.EqualityFilter;
import nl.azwaan.quotedb.api.filters.FilterBuilder;
import nl.azwaan.quotedb.api.patches.BookPatch;
import nl.azwaan.quotedb.dao.AuthorsDAO;
import nl.azwaan.quotedb.dao.BookQuotesDAO;
import nl.azwaan.quotedb.dao.BooksDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.BookQuote;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.permissions.PermissionChecker;
import org.jooby.Request;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.Collections;

@Singleton
@Path("/books")
@Produces("application/json")
@Consumes("application/json")
public class BooksAPI extends BaseAPI<Book, BookPatch> {

    @Inject private AuthorsDAO authorsDAO;
    @Inject private PermissionChecker<Author> authorPermissionChecker;
    @Inject private BookQuotesDAO bookQuotesDAO;
    @Inject private PermissionChecker<BookQuote> bookQuotePermissionChecker;

    @Inject
    protected BooksAPI(BooksDAO dao) {
        super(dao);
    }

    @Override
    protected void resolveReferencesForNewEntity(Book entity, User authenticatedUser) {
        super.resolveReferencesForNewEntity(entity, authenticatedUser);

        // If author already in db, replace with real entity
        authorsDAO.getEntityById(entity.getAuthor().getId())
                .ifPresent(entity::setAuthor);
    }

    @Override
    protected void applyPatch(Book book, User authenticatedUser, BookPatch patch) {
        patch.title.ifPresent(book::setTitle);
        patch.publisher.ifPresent(book::setPublisher);
        patch.publicationYear.ifPresent(book::setPublicationYear);
        patch.author.ifPresent(authorId -> {
            final Author author = authorsDAO.getEntityById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException(Author.class.getName(), authorId));
            authorPermissionChecker.checkReadEntity(author, authenticatedUser);
            book.setAuthor(author);
        });
    }

    @Override
    protected Class<BookPatch> getPatchClass() {
        return BookPatch.class;
    }

    @Override
    protected FilterBuilder getDefaultFilterBuilder(Request request) {
        return new BookFilterBuilder(dao, getAuthenticatedUser(request), request);
    }

    /**
     * Returns all quotes from a given book.
     * @param request The request that is handled
     * @param id The book id url parameter.
     * @return A page containing all the quotes in the book.
     */
    @GET
    @Path("/:id")
    public MultiResultPage<BookQuote> getQuotesFromBook(Request request, Long id) {
        final BookQuoteFilterBuilder filterBuilder = new BookQuoteFilterBuilder(bookQuotesDAO,
                getAuthenticatedUser(request), request);
        filterBuilder.addFilters(Collections.singletonList(new EqualityFilter<>(BookQuote.BOOK_ID, id)));
        return getPagedResult(request, bookQuotesDAO, bookQuotePermissionChecker, filterBuilder);
    }
}
