package nl.azwaan.quotedb.integration.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
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

    @Test
    public void duplicateUsernameError() {
        final EntityStore store = app.require(EntityStore.class);
        User user = new User();
        user.setUserName("user1");
        user.setPassword(BCrypt.hashpw("pwd1", BCrypt.gensalt()));

        store.insert(user);

        postBase()
                .body("{\"userName\": \"user1\", \"password\": \"pwd2\"}")
                .post("/auth/register")
                .then()
                .assertThat()
                .statusCode(409)
                .body("userName", equalTo(null)).and()
                .body("password", equalTo(null));

        user = ((Result<User>) store.select(User.class)
                .get())
                .first();

        assertThat(user.getUserName(), equalTo("user1"));
        assertThat(BCrypt.checkpw("pwd1", user.getPassword()), is(true));
    }

    @Test
    public void testSuccessfulAuthenticate() {
        final EntityStore store = app.require(EntityStore.class);
        User user = new User();
        user.setUserName("user1");
        user.setPassword(BCrypt.hashpw("pwd1", BCrypt.gensalt()));

        store.insert(user);
        store.refresh(user);

        String token = postBase()
                .body("{\"userName\": \"user1\", \"password\": \"pwd1\"}")
                .post("/auth/login")
                .then()
                .assertThat()
                .statusCode(202)
                .extract()
                .path("token");

        DecodedJWT dt = JWT.decode(token);
        assertThat(dt.getClaim("user_id").asString(), equalTo(user.getId().toString()));

    }

    @Test
    public void testFailedAuthenticate() {
        final EntityStore store = app.require(EntityStore.class);
        User user = new User();
        user.setUserName("user1");
        user.setPassword(BCrypt.hashpw("pwd1", BCrypt.gensalt()));

        store.insert(user);
        store.refresh(user);

        postBase()
                .body("{\"userName\": \"user1\", \"password\": \"pwd2\"}")
                .post("/auth/login")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    public void testFailedNoPassword() {
        final EntityStore store = app.require(EntityStore.class);
        User user = new User();
        user.setUserName("user1");
        user.setPassword(BCrypt.hashpw("pwd1", BCrypt.gensalt()));

        store.insert(user);
        store.refresh(user);

        postBase()
                .body("{\"userName\": \"user1\"}")
                .post("/auth/login")
                .then()
                .assertThat()
                .statusCode(400)
                .body("token", equalTo(null));
    }

    @Test
    public void testFailedNoUsername() {
        final EntityStore store = app.require(EntityStore.class);
        User user = new User();
        user.setUserName("user1");
        user.setPassword(BCrypt.hashpw("pwd1", BCrypt.gensalt()));

        store.insert(user);
        store.refresh(user);

        postBase()
                .body("{\"password\": \"pwd1\"}")
                .post("/auth/login")
                .then()
                .assertThat()
                .statusCode(400)
                .body("token", equalTo(null));
    }

}
