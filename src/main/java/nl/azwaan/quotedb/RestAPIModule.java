package nl.azwaan.quotedb;

import com.google.inject.Binder;
import com.typesafe.config.Config;
import nl.azwaan.quotedb.api.AllQuotesAPI;
import nl.azwaan.quotedb.api.AuthorsAPI;
import nl.azwaan.quotedb.api.BookQuotesAPI;
import nl.azwaan.quotedb.api.BooksAPI;
import nl.azwaan.quotedb.api.LabelsAPI;
import nl.azwaan.quotedb.api.QuotesAPI;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.Router;

import javax.annotation.Nonnull;

/**
 * Module controlling the REST API ({@code /api/**} routes).
 *
 * @author Aron Zwaan
 */
public class RestAPIModule implements Jooby.Module {

    /**
     * Configures REST API by adding {@link QuotesAPI} and {@link LabelsAPI} modules.
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

        router.use("/api", QuotesAPI.class);
        router.use("/api", AllQuotesAPI.class);
        router.use("/api", LabelsAPI.class);
        router.use("/api", AuthorsAPI.class);
        router.use("/api", BooksAPI.class);
        router.use("/api", BookQuotesAPI.class);
    }
}
