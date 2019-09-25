package nl.azwaan.quotedb.integration.api;

import io.requery.EntityStore;
import io.requery.query.Result;
import nl.azwaan.quotedb.models.User;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class AuthAPITests extends APITest {

    @Test
    public void testRegister() {
        postBase()
                .body("{\"userName\": \"user1\", \"password\": \"pwd1\"}")
                .post("/auth/register")
                .then()
                .assertThat()
                .statusCode(201)
                .body("userName", equalTo("user1")).and()
                .body("password", equalTo(null)); // Hashed pwd should not be published

        final EntityStore store = app.require(EntityStore.class);

        final User user = ((Result<User>) store.select(User.class)
                .get())
                .first();

        assertThat(user.getUserName(), equalTo("user1"));
        assertThat(BCrypt.checkpw("pwd1", user.getPassword()), is(true));

    }

}
