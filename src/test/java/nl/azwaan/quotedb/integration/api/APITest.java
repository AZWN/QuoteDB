package nl.azwaan.quotedb.integration.api;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import nl.azwaan.quotedb.JoobyClearDatabaseRule;
import nl.azwaan.quotedb.QuoteDBApp;
import org.junit.Rule;

import static io.restassured.RestAssured.given;

public class APITest {
    protected QuoteDBApp app = new QuoteDBApp();

    @Rule
    public JoobyClearDatabaseRule bootstrap = new JoobyClearDatabaseRule(app);

    protected RequestSpecification postBase() {
        return given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON);
    }
}
