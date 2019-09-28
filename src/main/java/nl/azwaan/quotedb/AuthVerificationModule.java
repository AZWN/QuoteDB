package nl.azwaan.quotedb;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Binder;
import com.typesafe.config.Config;
import nl.azwaan.quotedb.api.AuthAPI;
import nl.azwaan.quotedb.exceptions.IncompleteTokenException;
import org.jooby.*;

import java.util.Map;

public class AuthVerificationModule implements Jooby.Module {

    private String endpoints;
    private String jwtHashKey;

    /**
     * Constructs a new module providing authentication varification.
     * @param endpoints The endpoint regex to apply verification to.
     */
    public AuthVerificationModule(String endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public void configure(Env env, Config conf, Binder binder) {
        this.jwtHashKey = conf.getString(Constants.JWT_HASH_KEY);

        final Router router = env.router();
        router.use(endpoints, (req, rsp, chain) -> {
            final Mutant authHeader = req.header("Authorization");
            if (!authHeader.isSet()) {
                rsp.redirect("/login");
                rsp.status(Status.UNAUTHORIZED);
                return;
            }

            final DecodedJWT jwt = JWT.require(Algorithm.HMAC512(jwtHashKey))
                    .build()
                    .verify(authHeader.value());

            final Map<String, Claim> claims = jwt.getClaims();
            if (!claims.containsKey(Constants.JWT_USER_ID_KEY)) {
                throw new IncompleteTokenException("Invalid JWT token: no user_id claim");
            }

            chain.next(req, rsp);
        });
    }
}
