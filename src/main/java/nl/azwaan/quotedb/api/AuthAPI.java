package nl.azwaan.quotedb.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.dao.UsersDAO;
import nl.azwaan.quotedb.exceptions.InvalidRequestException;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;
import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.Date;

@Singleton
@Path("/auth")
@Produces("application/json")
@Consumes("application/json")
public class AuthAPI {

    private static final long TWO_DAYS = 1000 * 60 * 60 * 48;
    private UsersDAO usersDAO;
    private String jwtHashKey;

    /**
     * Creates a new {@link User} controller.
     * @param usersDAO The object used to access user resources.
     * @param conf The configuration object to het hash key from.
     */
    @Inject
    public AuthAPI(UsersDAO usersDAO, Config conf) {
        this.usersDAO = usersDAO;
        this.jwtHashKey = conf.getString("auth.jwt.hash.key");
    }

    /**
     * Endpoint in which users can register themselves.
     *
     * @param creds The credentials for which a User should be created
     * @return {link Result} with CREATED if the user could be created.
     */
    @Path("/register")
    @POST
    public Result register(@Body Credentials creds) {
        if (creds.userName ==  null || creds.userName.isEmpty()) {
            throw new InvalidRequestException("No username provided");
        }

        if (creds.password ==  null || creds.password.isEmpty()) {
            throw new InvalidRequestException("No password provided");
        }

        final User user = usersDAO.insertUser(creds.userName, creds.password);
        final Result result = Results.ok(user);
        result.status(Status.CREATED);
        return result;
    }

    /**
     * Endpoint used for username/password authentication.
     * @param request The request, used to create a session for.
     * @param creds The credentials to authenticate with.
     * @return Appropriate response (code).
     * @throws InvalidRequestException When no username or no password is provided.
     */
    @Path("/login")
    @POST
    public Result login(Request request, @Body Credentials creds) {
        if (creds.userName ==  null || creds.userName.isEmpty()) {
            throw new InvalidRequestException("No username provided");
        }

        if (creds.password ==  null || creds.password.isEmpty()) {
            throw new InvalidRequestException("No password provided");
        }

        if (usersDAO.userHasPassword(creds.userName, creds.password)) {
            final User user = usersDAO.getUserByUserName(creds.userName);

            // 2 days from now
            final Date date = new Date();
            date.setTime(date.getTime() + TWO_DAYS);

            final String signedToken = JWT.create()
                    .withClaim(Constants.JWT_USER_ID_KEY, user.getId())
                    .withExpiresAt(date)
                    .sign(Algorithm.HMAC512(jwtHashKey));

            final Token token = new Token();
            token.token = signedToken;

            final Result res = Results.accepted(token);
            return res;
        }

        final Result res = new Result();
        res.status(Status.UNAUTHORIZED);
        return res;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Credentials {
        /**
         * The client-provided username.
         */
        public String userName;
        /**
         * The client-provided password.
         */
        public String password;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Token {
        /**
         * The value of the token.
         */
        public String token;
    }
}
