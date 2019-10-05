package nl.azwaan.quotedb.integration.api;

import io.requery.EntityStore;
import io.restassured.http.ContentType;
import nl.azwaan.quotedb.models.Quote;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class QuoteAPITests extends AuthenticatedTest {

    @Test
    public void testGetLabelsNoLabels() throws Throwable {
        getBase()
                .get("/api/quotes")
                .then()
                .assertThat()
                .body("size()", equalTo(0));
    }

    @Test
    public void testDefaultFirstPage() {
        insertQuotes(120);

        getBase()
                .get("/api/quotes")
                .then()
                .assertThat()
                .body("size()", equalTo(100))
                .and().body("[0].text", equalTo("TestQuote1"))
                .and().body("[99].text", equalTo("TestQuote100"));

    }

    @Test
    public void testSecondPage() {
        insertQuotes(120);
        getBase()
                .get("/api/quotes?page=2")
                .then()
                .assertThat()
                .body("size()", equalTo(20))
                .and().body("[0].text", equalTo("TestQuote101"))
                .and().body("[19].text", equalTo("TestQuote120"));

    }

    @Test
    public void testPageSize() {
        insertQuotes(50);
        getBase()
                .get("/api/quotes?page=2&pageSize=20")
                .then()
                .assertThat()
                .body("size()", equalTo(20))
                .and().body("[0].text", equalTo("TestQuote21"))
                .and().body("[19].text", equalTo("TestQuote40"));

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
    public void testCreateQuote() {
        Quote quote = new Quote();
        quote.setText("My super fancy quote");

        postBase()
                .body(quote)
                .post("/api/quotes")
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(201);
    }


    private void insertQuotes(int quoteCount) {
        EntityStore store = app.require(EntityStore.class);

        for (int i = 1; i <= quoteCount; i++) {
            Quote quote = new Quote();
            quote.setText(String.format("TestQuote%d", i));

            store.insert(quote);
        }
    }
}
