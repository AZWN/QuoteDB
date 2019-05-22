package nl.azwaan.quotedb;

import io.requery.sql.TableCreationMode;

import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.assets.Assets;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.jooby.pebble.Pebble;
import org.jooby.requery.Requery;

import nl.azwaan.quotedb.models.Models;
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
                .schema(TableCreationMode.CREATE_NOT_EXISTS));

        use(new RestAPIModule());

        // React frontend setup
        use(new Assets());

        use(new Pebble());
        get("/", () -> Results.html("index"));
    }

    /**
     * Method starting application server.
     * @param args Command line arguments
     */
    public static void main(final String[] args) {
        run(QuoteDBApp::new, args);
    }

}
