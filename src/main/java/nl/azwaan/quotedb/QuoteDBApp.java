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

    // To remember:
    // - Possible using MVC api is most composable

    {
        use(new Jackson());

        /* String osName = System.getProperty("os.name");
        String dbConf = osName.equals("Linux") ? "db_linux" : "db_win"; */
        String dbConf = "db";

        use(new Jdbc(dbConf));

        use(new Requery(dbConf, Models.DEFAULT)
                .schema(TableCreationMode.CREATE_NOT_EXISTS));

        use(new RestAPIModule());

        /* onStart(() -> {
            // Run liquibase update script
            DataSource dataSource = require(DataSource.class);
            Connection conn = dataSource.getConnection();
            Liquibase lb = new Liquibase("conf/liquibase.xml",
                    new FileSystemResourceAccessor(Paths.get("").toAbsolutePath().toString()),
                    new JdbcConnection(conn));
            lb.update(new Contexts());

        }); */

        /* path("/api", () -> {
            get("/categories", () -> {
                EntityStore store = require(EntityStore.class);
                Object res = store.select(Category.class).get();
                return res;
            });

            get("/quotes", () -> {
                EntityStore store = require(EntityStore.class);
                Object res =  store.select(Quote.class).get();
                return res;
            });

            get("/labels", () -> {
                EntityStore store = require(EntityStore.class);
                Object res =  store.select(Label.class).get();
                return res;
            });
        }); */
    }

    public static void main(final String[] args) {
        run(QuoteDBApp::new, args);
    }

}
