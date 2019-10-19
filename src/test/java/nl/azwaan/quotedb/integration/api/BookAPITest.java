package nl.azwaan.quotedb.integration.api;

import io.requery.EntityStore;
import io.requery.query.Scalar;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.User;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BookAPITest extends AuthenticatedTest {

    @Test
    public void insertWithExistingAuthor() throws ParseException {
        EntityStore store = app.require(EntityStore.class);
        User user = getUser();

        Author author = new Author();
        author.setUser(user);
        author.setFirstName("Charles");
        author.setMiddleName("");
        author.setLastName("Dickens");
        author.setInitials("C.J.H.");
        author.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").parse("07-02-1812"));

        store.insert(author);
        store.refresh(author);

        Book book = new Book();
        book.setAuthor(author);
        book.setTitle("Adventures of Huckleberry Finn");
        book.setPublicationYear(1884);
        book.setPublisher("Chatto & Windus / Charles L. Webster And Company");

        postBase()
                .body(book)
                .post("api/books")
                .then()
                .statusCode(201);

        assertThat(((Scalar<Integer>)store.count(Author.class).get()).value(), equalTo(1));
    }

    @Test
    public void insertWithNewAuthor() throws ParseException {
        EntityStore store = app.require(EntityStore.class);
        User user = getUser();

        Author author = new Author();
        author.setUser(user);
        author.setFirstName("Charles");
        author.setMiddleName("");
        author.setLastName("Dickens");
        author.setInitials("C.J.H.");
        author.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").parse("07-02-1812"));

        // No author insert

        Book book = new Book();
        book.setAuthor(author);
        book.setTitle("Adventures of Huckleberry Finn");
        book.setPublicationYear(1884);
        book.setPublisher("Chatto & Windus / Charles L. Webster And Company");

        postBase()
                .body(book)
                .post("api/books")
                .then()
                .statusCode(201);

        // Assert author inserted
        assertThat(((Scalar<Integer>)store.count(Author.class).get()).value(), equalTo(1));
    }
}
