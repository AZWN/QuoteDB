package nl.azwaan.quotedb;

import io.requery.EntityStore;
import io.requery.sql.TableCreationMode;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;

import nl.azwaan.quotedb.models.Quote;
import org.jooby.Jooby;
import org.jooby.jdbc.Jdbc;
import org.jooby.json.Jackson;
import org.jooby.requery.Requery;

import nl.azwaan.quotedb.models.Models;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;

/**
 * AbstractQuote database
 */
public class App extends Jooby {

    {
        use(new Jackson());

        use(new Jdbc());

        use(new Requery(Models.DEFAULT)
                .schema(TableCreationMode.CREATE_NOT_EXISTS));

        onStart(() -> {
            // Print classpath
            String lbconf = Paths.get("", "conf", "liquibase.xml")
                    .toAbsolutePath().toString();

            System.out.println(lbconf);

            // Run liquibase update script
            DataSource dataSource = require(DataSource.class);
            Connection conn = dataSource.getConnection();
            Liquibase lb = new Liquibase("conf/liquibase.xml",
                    new FileSystemResourceAccessor(Paths.get("").toAbsolutePath().toString()),
                    new JdbcConnection(conn));
            lb.update(new Contexts());

            // Insert sample Quote
            EntityStore store = require(EntityStore.class);

            Quote quote = new Quote();
            quote.author = "Aron Zwaan";
            quote.source = "Mouth";
            quote.text = "Draai een volle mok nooit om";

            store.insert(quote);
        });

        get("/quotes", () -> {
            EntityStore store = require(EntityStore.class);
            return store.select(Quote.class).get();
        });

    }

    public static void main(final String[] args) {
        run(App::new, args);
    }

}
