package nl.azwaan.quotedb;

import com.google.inject.Binder;
import com.typesafe.config.Config;
import nl.azwaan.quotedb.api.CategoriesAPI;
import nl.azwaan.quotedb.api.LabelsAPI;
import nl.azwaan.quotedb.api.QuotesAPI;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.Router;

import javax.annotation.Nonnull;

public class RestAPIModule implements Jooby.Module {

    @Override
    public void configure(@Nonnull Env env, @Nonnull Config conf, @Nonnull Binder binder) {
        Router router = env.router();
        router.use("/api", CategoriesAPI.class);
        router.use("/api", QuotesAPI.class);
        router.use("/api", LabelsAPI.class);

    }
}
