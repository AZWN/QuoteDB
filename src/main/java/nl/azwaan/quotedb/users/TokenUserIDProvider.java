package nl.azwaan.quotedb.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.Constants;
import nl.azwaan.quotedb.exceptions.IncompleteTokenException;
import org.jooby.Mutant;
import org.jooby.Request;

import java.util.Map;

@Singleton
public class TokenUserIDProvider implements UserIDProvider {


    /**
     * Constructs a new {@link UserIDProvider} that checks for a userID in the token in the Authorization header.
     */
    @Inject
    public TokenUserIDProvider() { }

    @Override
    public Long getUserId(Request req) {
        final Mutant authHeader = req.header("Authorization");
        if (!authHeader.isSet()) {
            throw new RuntimeException("User id requested from unauthorized request");
        }

        final DecodedJWT jwt = JWT.decode(authHeader.value());

        final Map<String, Claim> claims = jwt.getClaims();
        if (!claims.containsKey(Constants.JWT_USER_ID_KEY)) {
            throw new IncompleteTokenException("Invalid JWT token: no user_id claim");
        }

        return claims.get(Constants.JWT_USER_ID_KEY).asLong();
    }
}
