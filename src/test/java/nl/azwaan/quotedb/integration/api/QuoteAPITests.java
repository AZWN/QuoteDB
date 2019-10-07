package nl.azwaan.quotedb.integration.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.requery.EntityStore;
import io.requery.Persistable;
import io.restassured.http.ContentType;
import nl.azwaan.quotedb.models.QuickQuote;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class QuoteAPITests extends AuthenticatedTest {

    @Test
    public void testGetLabelsNoLabels() {
        getBase()
                .get("/api/quotes")
                .then()
                .assertThat()
                .body("data.size()", equalTo(0));
    }

    @Test
    public void testDefaultFirstPage() {
        insertQuotes(120);

        getBase()
                .get("/api/quotes")
                .then()
                .assertThat()
                .body("data.size()", equalTo(100))
                .and().body("data[0].text", equalTo("TestQuote1"))
                .and().body("data[99].text", equalTo("TestQuote100"));

    }

    @Test
    public void testSecondPage() {
        insertQuotes(120);
        getBase()
                .get("/api/quotes?page=2")
                .then()
                .assertThat()
                .body("data.size()", equalTo(20))
                .and().body("data[0].text", equalTo("TestQuote101"))
                .and().body("data[19].text", equalTo("TestQuote120"));

    }

    @Test
    public void testPageSize() {
        insertQuotes(50);
        getBase()
                .get("/api/quotes?page=2&pageSize=20")
                .then()
                .assertThat()
                .body("data.size()", equalTo(20))
                .and().body("data[0].text", equalTo("TestQuote21"))
                .and().body("data[19].text", equalTo("TestQuote40"));

    }

    @Test
    public void testPageSizeOverflow() {
        insertQuotes(120);
        getBase()
                .get("/api/quotes?pageSize=120")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testPageSizeUnderflow() {
        insertQuotes(120);

        getBase()
                .get("/api/quotes?pageSize=0")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testCreateQuote() throws IOException {
        QuickQuote quote = new QuickQuote();
        quote.setText("My super fancy quote");
        quote.setNote("Note");
        quote.setTitle("Quote title");

        postBase()
                .body(quote)
                .post("/api/quotes")
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(201);

        EntityStore<Persistable, QuickQuote> store = app.require(EntityStore.class);
        QuickQuote result = store.select(QuickQuote.class)
                .get()
                .first();

        assertThat(result.getText(), equalTo("My super fancy quote"));
        assertThat(result.getNote(), equalTo("Note"));
        assertThat(result.getTitle(), equalTo("Quote title"));

        assertThat(result.getId(), is(not(nullValue())));
        assertThat(result.getGenerationDate(), is(not(nullValue())));
        assertThat(result.getLastModifiedDate(), is(not(nullValue())));
    }


    private void insertQuotes(int quoteCount) {
        EntityStore store = app.require(EntityStore.class);

        for (int i = 1; i <= quoteCount; i++) {
            QuickQuote quote = new QuickQuote();
            quote.setText(String.format("TestQuote%d", i));
            quote.setTitle(String.format("Title%d", i));

            store.insert(quote);
        }
    }
}
