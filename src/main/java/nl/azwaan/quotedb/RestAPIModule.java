package nl.azwaan.quotedb;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.inject.Binder;
import com.typesafe.config.Config;
import nl.azwaan.quotedb.api.CategoriesAPI;
import nl.azwaan.quotedb.api.LabelsAPI;
import nl.azwaan.quotedb.api.QuotesAPI;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.Mutant;
import org.jooby.Router;
import org.jooby.Status;
// import org.jooby.Status;

import javax.annotation.Nonnull;

// import static nl.azwaan.quotedb.Constants.USER_ID_SESSION_KEY;

/**
 * Module controlling the REST API ({@code /api/**} routes).
 *
 * @author Aron Zwaan
 */
public class RestAPIModule implements Jooby.Module {

    /**
     * Configures REST API by adding {@link QuotesAPI}, {@link CategoriesAPI} and {@link LabelsAPI} modules.
     * @param env The environment the module is configured in
     * @param conf The configuration for the module. Currently not used
     * @param binder The Guice binder. Currently not used.
     *
     */
    @Override
    public void configure(@Nonnull Env env, @Nonnull Config conf,
                          @Nonnull Binder binder)
    {
        final Router router = env.router();
        router.use("/api/**", (req, rsp, chain) -> {
            final Mutant authHeader = req.header("Authorization");
            if (!authHeader.isSet()) {
                rsp.redirect("/login");
                rsp.status(Status.UNAUTHORIZED);
                return;
            }

            final DecodedJWT decodedToken = JWT.require(Algorithm.HMAC512(Constants.JWT_HASH_KEY))
                    .build()
                    .verify(authHeader.value());

            chain.next(req, rsp);
        });

        router.use("/api", CategoriesAPI.class);
        router.use("/api", QuotesAPI.class);
        router.use("/api", LabelsAPI.class);
    }
}
