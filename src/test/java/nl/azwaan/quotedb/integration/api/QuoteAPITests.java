package nl.azwaan.quotedb.integration.api;

import io.requery.EntityStore;
import nl.azwaan.quotedb.JoobyClearDatabaseRule;
import nl.azwaan.quotedb.QuoteDBApp;
import nl.azwaan.quotedb.models.Category;
import nl.azwaan.quotedb.models.Quote;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

public class QuoteAPITests {
    private QuoteDBApp app = new QuoteDBApp();

    @Rule
    public JoobyClearDatabaseRule bootstrap = new JoobyClearDatabaseRule(app);

    private Category cat;

    @Before
    public void createCategory() {
        EntityStore store = app.require(EntityStore.class);

        cat = new Category();
        cat.setName("Test Category");

        store.insert(cat);
        store.refresh(cat);
    }

    @Test
    public void testGetLabelsNoLabels() throws Throwable {
        String url = String.format("/api/categories/%d/quotes", cat.id);
        get(url)
                .then()
                .assertThat()
                .body("size()", equalTo(0));
    }

    @Test
    public void testDefaultFirstPage() {
        insertQuotes(120);
        String url = String.format("/api/categories/%d/quotes", cat.id);
        get(url)
                .then()
                .assertThat()
                .body("size()", equalTo(100))
                .and().body("[0].text", equalTo("TestQuote1"))
                .and().body("[99].text", equalTo("TestQuote100"));

    }

    @Test
    public void testSecondPage() {
        insertQuotes(120);
        String url = String.format("/api/categories/%d/quotes?page=2", cat.id);
        get(url)
                .then()
                .assertThat()
                .body("size()", equalTo(20))
                .and().body("[0].text", equalTo("TestQuote101"))
                .and().body("[19].text", equalTo("TestQuote120"));

    }

    @Test
    public void testPageSize() {
        insertQuotes(50);
        String url = String.format("/api/categories/%d/quotes?page=2&pageSize=20", cat.id);
        get(url)
                .then()
                .assertThat()
                .body("size()", equalTo(20))
                .and().body("[0].text", equalTo("TestQuote21"))
                .and().body("[19].text", equalTo("TestQuote40"));

    }

    @Test
    public void testPageSizeOverflow() {
        insertQuotes(120);
        String url = String.format("/api/categories/%d/quotes?pageSize=120", cat.id);
        get(url)
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testPageSizeUnderflow() {
        insertQuotes(120);
        String url = String.format("/api/categories/%d/quotes?pageSize=0", cat.id);
        get(url)
                .then()
                .assertThat()
                .statusCode(400);
    }

    private void insertQuotes(int quoteCount) {
        EntityStore store = app.require(EntityStore.class);

        for (int i = 1; i <= quoteCount; i++) {
            Quote quote = new Quote();
            quote.setAuthor("Test");
            quote.setCategory(cat);
            quote.setSource("test");
            quote.setText(String.format("TestQuote%d", i));

            store.insert(quote);
        }
    }
}