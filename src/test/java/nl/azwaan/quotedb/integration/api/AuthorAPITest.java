package nl.azwaan.quotedb.integration.api;

import io.requery.EntityStore;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.User;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class AuthorAPITest extends AuthenticatedTest {

    @Test
    public void testPatchBio() throws ParseException {
        User user = getUser();

        Author author = new Author();
        author.setUser(user);
        author.setFirstName("Charles");
        author.setMiddleName("");
        author.setLastName("Dickens");
        author.setInitials("C.J.H.");
        author.setDateOfBirth(new SimpleDateFormat("dd-MM-yyyy").parse("07-02-1812"));

        EntityStore store = app.require(EntityStore.class);
        store.insert(author);
        store.refresh(author);

        // Verify no bio is set via api
        getBase()
                .get("/api/authors")
                .then()
                .statusCode(200)
                .assertThat()
                .body("data.size()", equalTo(1))
                .body("data[0].biography", nullValue());

        // Perform bio patch
        patchBase()
                .body("{\"biography\": \"Some biographical content\", \"middleName\": \"the\"}")
                .patch(String.format("/api/authors/%d", author.getId()))
                .then()
                .statusCode(200);

        // Check patch applied and all properties the same
        getBase()
                .get("/api/authors")
                .then()
                .statusCode(200)
                .assertThat()
                .body("data.size()", equalTo(1))
                .body("data[0].biography", equalTo("Some biographical content"))
                .body("data[0].firstName", equalTo("Charles"))
                .body("data[0].middleName", equalTo("the"))
                .body("data[0].lastName", equalTo("Dickens"))
                .body("data[0].initials", equalTo("C.J.H."))
                .body("data[0].dateOfBirth", equalTo("1812-02-07"))
                .body("data[0].user", nullValue());
    }
}
