package nl.azwaan.quotedb.integration.api;

import nl.azwaan.quotedb.JoobyClearDatabaseRule;
import nl.azwaan.quotedb.QuoteDBApp;
import org.junit.Rule;

public class APITest {
    protected QuoteDBApp app = new QuoteDBApp();

    @Rule
    public JoobyClearDatabaseRule bootstrap = new JoobyClearDatabaseRule(app);
}
