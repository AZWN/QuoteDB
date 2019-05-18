package nl.azwaan.quotedb;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import nl.azwaan.quotedb.dao.QuoteDAO;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jooby.Jooby;
import org.jooby.jdbc.Jdbc;
import org.jooby.jdbi.Jdbi3;
import org.jooby.jdbi.TransactionalRequest;
import org.jooby.json.Jackson;

import javax.sql.DataSource;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.sql.Connection;

/**
 * Quote database
 */
public class App extends Jooby {

    {
        use(new Jackson());

        use(new Jdbc());

        use(new Jdbi3()
        /* Install SqlObjectPlugin */
            .doWith(jdbi -> {
                jdbi.installPlugin(new SqlObjectPlugin());
            })
        /* Creates a transaction per request and attach QuoteDAO */
            .transactionPerRequest(
                new TransactionalRequest()
                    .attach(QuoteDAO.class)
            )
        );

    /* Create database */
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
        });

    }

    public static void main(final String[] args) {
        run(App::new, args);
    }

}
