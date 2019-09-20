package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.dao.UsersDAO;
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

@Singleton
@Path("/auth")
@Produces("application/json")
@Consumes("application/json")
public class AuthAPI {

    private UsersDAO usersDAO;

    /**
     * Creates a new {@link User} controller.
     * @param usersDAO The object used to access user resources.
     */
    @Inject
    public AuthAPI(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
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
     */
    @Path("/login")
    @POST
    public Result login(Request request, @Body Credentials creds) {
        final Result res = new Result();
        if (usersDAO.userHasPassword(creds.userName, creds.password)) {
            final User user = usersDAO.getUserByUserName(creds.userName);
            request.session().set(Constants.USER_ID_SESSION_KEY, user.id);
            res.status(Status.ACCEPTED);
            return res;
        }

        res.status(Status.UNAUTHORIZED);
        return res;
    }

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
}
