package nl.azwaan.quotedb.users;

import org.jooby.Request;

public interface UserIDProvider {

    /**
     * Returns the user id of the authenticated user from the request.
     * @param request The request to get the user id from.
     * @return The Id of the user.
     */
    Long getUserId(Request request);
}
