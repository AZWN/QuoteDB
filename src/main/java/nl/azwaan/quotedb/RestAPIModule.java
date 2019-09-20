package nl.azwaan.quotedb;

import com.google.inject.Binder;
import com.typesafe.config.Config;
import nl.azwaan.quotedb.api.CategoriesAPI;
import nl.azwaan.quotedb.api.LabelsAPI;
import nl.azwaan.quotedb.api.QuotesAPI;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.Router;
import org.jooby.Status;

import javax.annotation.Nonnull;

import static nl.azwaan.quotedb.Constants.USER_ID_SESSION_KEY;

/**
 * Module controlling the REST API ({@code /api/**} routes).
 *
 * @author Aron Zwaan
 */
public class RestAPIModule implements Jooby.Module {

    /**
     * Configures REST API by adding {@link QuotesAPI}, {@link CategoriesAPI} and {@link LabelsAPI} modules.
     * @param env The environent the module is configured in
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
            if (!req.session().isSet(USER_ID_SESSION_KEY)) {
                rsp.redirect(Status.UNAUTHORIZED, "/auth/login");
                return;
            }

            chain.next(req, rsp);
        });

        router.use("/api", CategoriesAPI.class);
        router.use("/api", QuotesAPI.class);
        router.use("/api", LabelsAPI.class);
    }
}
