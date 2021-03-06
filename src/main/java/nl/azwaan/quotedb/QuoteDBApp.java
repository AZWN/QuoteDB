package nl.azwaan.quotedb;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.requery.sql.TableCreationMode;
import io.requery.sql.TransactionMode;

import nl.azwaan.quotedb.users.DevUserIDProvider;
import nl.azwaan.quotedb.users.TokenUserIDProvider;
import nl.azwaan.quotedb.users.UserIDProvider;

import org.jooby.Jooby;
import org.jooby.flyway.Flywaydb;
import org.jooby.frontend.Npm;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.jooby.requery.Requery;

import java.text.SimpleDateFormat;

/**
 * Quote database Application.
 *
 * @author Aron Zwaan
 */
public class QuoteDBApp extends Jooby {

    {
        // REST API backend setup
        use(new Jackson()
                .module(new Jdk8Module())
                .doWith(mapper -> {
                    final SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    mapper.setDateFormat(df);
                }));

        use(new Jdbc());
        use(new Flywaydb());
        use(new Requery(Models.DEFAULT)
                .schema(TableCreationMode.CREATE_NOT_EXISTS)
                .doWith(s -> s.setTransactionMode(TransactionMode.NONE)));

        // React frontend setup
        on("dev", "dev-no-auth", "", () -> {
            use(new Npm("v10.17.0"));
        });

        on("dev-no-auth", () -> {
            bind(UserIDProvider.class, DevUserIDProvider.class);
        }).orElse(() -> {
            use(new AuthModule());
            use(new AuthVerificationModule("/api/**"));
            bind(UserIDProvider.class, TokenUserIDProvider.class);
        });

        use(new PermissionsModule());

        use(new RestAPIModule());

        assets("/dist/**", "/dist/{0}");
        assets("/**", "dist/index.html");
    }

    /**
     * Method starting application server.
     * @param args Command line arguments
     */
    public static void main(final String[] args) {
        run(QuoteDBApp::new, args);
    }

}
