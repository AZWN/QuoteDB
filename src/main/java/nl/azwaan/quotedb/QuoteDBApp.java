package nl.azwaan.quotedb;

import io.requery.sql.TableCreationMode;
import io.requery.sql.TransactionMode;

import nl.azwaan.quotedb.models.Models;
import nl.azwaan.quotedb.users.DevUserIDProvider;
import nl.azwaan.quotedb.users.TokenUserIDProvider;
import nl.azwaan.quotedb.users.UserIDProvider;

import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.jooby.pebble.Pebble;
import org.jooby.requery.Requery;

/**
 * Quote database Application.
 *
 * @author Aron Zwaan
 */
public class QuoteDBApp extends Jooby {

    {
        // REST API backend setup
        use(new Jackson());

        use(new Jdbc());

        use(new Requery(Models.DEFAULT)
                .schema(TableCreationMode.CREATE_NOT_EXISTS)
                .doWith(s -> s.setTransactionMode(TransactionMode.NONE)));

        // React frontend setup
        // use(new Assets());

        use(new Pebble());
        get("/", () -> Results.html("index"));

        on("dev", () -> {
            bind(UserIDProvider.class, DevUserIDProvider.class);
        }).orElse(() -> {
            use(new AuthModule());
            use(new AuthVerificationModule("/api/**"));
            bind(UserIDProvider.class, TokenUserIDProvider.class);
        });

        use(new PermissionsModule());

        use(new RestAPIModule());
    }

    /**
     * Method starting application server.
     * @param args Command line arguments
     */
    public static void main(final String[] args) {
        run(QuoteDBApp::new, args);
    }

}
