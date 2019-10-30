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
public class BooksAPI extends BaseAPI<Book, BookPatch> {

    @Inject private AuthorsDAO authorsDAO;

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
            // TODO: Permissions
            final Author author = authorsDAO.getEntityById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException(Author.class.getName(), authorId));
            book.setAuthor(author);
        });
    }

    @Override
    protected Class<BookPatch> getPatchClass() {
        return BookPatch.class;
    }

}
