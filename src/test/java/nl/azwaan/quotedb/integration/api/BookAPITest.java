package nl.azwaan.quotedb.integration.api;

import io.requery.EntityStore;
import io.requery.query.Scalar;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.User;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BookAPITest extends AuthenticatedTest {

    private User user;
    private Author author1;
    private Author author2;

    @Before
    public void insertAuthors() throws ParseException {
        EntityStore store = app.require(EntityStore.class);
        user = getUser();

        author1 = new Author();
        author1.setUser(user);
        author1.setFirstName("Charles");
        author1.setMiddleName("");
        author1.setLastName("Dickens");
        author1.setInitials("C.J.H.");
        author1.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").parse("07-02-1812"));

        store.insert(author1);
        store.refresh(author1);

        author2 = new Author();
        author2.setUser(user);
        author2.setFirstName("Samuel Langhorne");
        author2.setMiddleName("");
        author2.setLastName("Clemens");
        author2.setInitials("S.L.");
        author2.setBiography("Better known as Mark Twain.");
        author2.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").parse("07-02-1812"));

        store.insert(author2);
        store.refresh(author2);
    }

    @Test
    public void insertWithExistingAuthor() {
        EntityStore store = app.require(EntityStore.class);

        Book book = new Book();
        book.setAuthor(author1);
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
        author.setFirstName("Thomas");
        author.setMiddleName("");
        author.setLastName("Hobbes");
        author.setInitials("T.");
        author.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").parse("05-04-1588"));

        Book book = new Book();
        book.setAuthor(author);
        book.setTitle("The Questions concerning Liberty, Necessity and Chance");
        book.setPublicationYear(1841);
        book.setPublisher("");

        postBase()
                .body(book)
                .post("api/books")
                .then()
                .statusCode(201);

        // Assert author inserted
        assertThat(((Scalar<Integer>)store.count(Author.class).where(Author.LAST_NAME.eq("Hobbes")).get()).value(), equalTo(1));
    }

    @Test
    public void testPatchTitle() {
        EntityStore store = app.require(EntityStore.class);

        Book book = new Book();
        book.setUser(user);
        book.setAuthor(author1);
        book.setTitle("Adventures of Huckleberry Finnn");
        book.setPublicationYear(1884);
        book.setPublisher("Chatto & Windus / Charles L. Webster And Company");

        store.insert(book);

        postBase()
                .body("{\"title\": \"Adventures of Huckleberry Finn\"}")
                .patch(String.format("/api/books/%d", book.getId()))
                .then()
                .statusCode(200);

        store.refresh(book);
        assertThat(book.getTitle(), equalTo("Adventures of Huckleberry Finn"));
    }

    @Test
    public void testPatchAuthor() {
        EntityStore store = app.require(EntityStore.class);
        User user = getUser();

        Book book = new Book();
        book.setUser(user);
        book.setAuthor(author1);
        book.setTitle("Adventures of Huckleberry Finn");
        book.setPublicationYear(1884);
        book.setPublisher("Chatto & Windus / Charles L. Webster And Company");

        store.insert(book);

        postBase()
                .body(String.format("{\"author\": %d}", author2.getId()))
                .patch(String.format("/api/books/%d", book.getId()))
                .then()
                .statusCode(200);

        store.refresh(book);
        assertThat(book.getAuthor(), equalTo(author2));
    }
}
