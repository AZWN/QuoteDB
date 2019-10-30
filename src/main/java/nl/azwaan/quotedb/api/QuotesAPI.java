package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.api.filters.FilterBuilder;
import nl.azwaan.quotedb.api.filters.QuickQuoteFilter;
import nl.azwaan.quotedb.api.patches.QuotePatch;
import nl.azwaan.quotedb.dao.QuotesDAO;
import nl.azwaan.quotedb.models.QuickQuote;
import org.jooby.Request;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

/**
 * MVC Controller for manipulating quotes.
 *
 * @author Aron Zwaan
 */
@Singleton
@Path("/quotes")
@Produces("application/json")
@Consumes("application/json")
public class QuotesAPI extends BaseQuoteAPI<QuickQuote, QuotePatch> {
    /**
     * Creates a new {@link QuotesAPI} controller.
     * @param quotesDAO The db accessor for quotes.
     */
    @Inject
    public QuotesAPI(QuotesDAO quotesDAO) {
        super(quotesDAO);
    }

    @Override
    protected Class<QuotePatch> getPatchClass() {
        return QuotePatch.class;
    }

    @Override
    protected FilterBuilder getDefaultFilterBuilder(Request request) {
        return new QuickQuoteFilter(dao, getAuthenticatedUser(request), request);
    }
}
