package nl.azwaan.quotedb.integration.api;

import io.requery.EntityStore;
import io.requery.query.Result;
import io.requery.query.Scalar;
// import nl.azwaan.quotedb.models.AllQuotesView;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.BaseQuote;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.BookQuote;
import nl.azwaan.quotedb.models.QuickQuote;
import nl.azwaan.quotedb.models.User;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ViewTest extends AuthenticatedTest {

    @Test
    public void testSelectQuoteUnion() throws ParseException {
        User user = getUser();
        EntityStore store = app.require(EntityStore.class);

        QuickQuote quote = new QuickQuote();
        quote.setUser(user);
        quote.setText("Quote");
        quote.setTitle("Quick");
        store.insert(quote);

        Author author = new Author();
        author.setFirstName("Charles");
        author.setLastName("Dickens");
        author.setInitials("C.H.S.");
        author.setMiddleName("");
        author.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("1882-04-05"));
        author.setUser(user);
        store.insert(author);

        Book book = new Book();
        book.setUser(user);
        book.setTitle("A christmas carol");
        book.setPublisher("<unknown>");
        book.setPublicationYear(1932);
        book.setAuthor(author);
        store.insert(book);

        BookQuote bquote = new BookQuote();
        bquote.setBook(book);
        bquote.setUser(user);
        bquote.setText("It was a cold and snowy morning");
        bquote.setTitle("Morning snow");
        store.insert(bquote);

        assertThat(((Result<BaseQuote>)store.raw(QuickQuote.class, "select id, deleted, generationdate, lastmodifieddate, note, text, title, owner from quickquote union select id, deleted, generationdate, lastmodifieddate, note, text, title, owner from bookquote")).toList().size(), equalTo(2));
//
//         assertThat(((Scalar<Integer>) store.count(AllQuotesView.class).get()).value(), equalTo(2));
    }
}
