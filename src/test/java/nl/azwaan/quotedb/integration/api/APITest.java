package nl.azwaan.quotedb.integration.api;

import io.requery.EntityStore;
import io.requery.query.Result;
import io.requery.query.Selection;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import nl.azwaan.quotedb.JoobyClearDatabaseRule;
import nl.azwaan.quotedb.QuoteDBApp;
import nl.azwaan.quotedb.models.User;
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

    protected User getUser() {
        EntityStore store = app.require(EntityStore.class);
        return ((Selection<Result<User>>) store.select(User.class))
                .get()
                .first();
    }
}
