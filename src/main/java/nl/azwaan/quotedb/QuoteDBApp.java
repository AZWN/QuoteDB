package nl.azwaan.quotedb;

import io.requery.sql.TableCreationMode;

import org.jooby.Jooby;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.jooby.requery.Requery;

import nl.azwaan.quotedb.models.Models;
/**
 * Quote database
 */
public class QuoteDBApp extends Jooby {

    {
        use(new Jackson());

        use(new Jdbc());

        use(new Requery(Models.DEFAULT)
                .schema(TableCreationMode.CREATE_NOT_EXISTS));

        use(new RestAPIModule());
    }

    public static void main(final String[] args) {
        run(QuoteDBApp::new, args);
    }

}
