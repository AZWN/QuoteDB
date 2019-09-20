package nl.azwaan.quotedb;

import com.google.inject.Binder;
import com.typesafe.config.Config;
import nl.azwaan.quotedb.api.AuthAPI;
import org.jooby.Env;
import org.jooby.Jooby;
import org.jooby.Router;

public class AuthModule implements Jooby.Module {

    @Override
    public void configure(Env env, Config conf, Binder binder) throws Throwable {
        final Router router = env.router();
        router.use(AuthAPI.class);
    }
}
