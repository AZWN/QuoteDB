package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.azwaan.quotedb.api.patches.QuotePatch;
import nl.azwaan.quotedb.dao.QuotesDAO;
import nl.azwaan.quotedb.exceptions.EntityNotFoundException;
import nl.azwaan.quotedb.models.QuickQuote;
import org.jooby.Request;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.PATCH;
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
public class QuotesAPI extends BaseAPI<QuickQuote> {

    /**
     * Creates a new {@link QuotesAPI} controller.
     * @param quotesDAO The db accessor for quotes.
     */
    @Inject
    public QuotesAPI(QuotesDAO quotesDAO) {
        super(quotesDAO);
    }

    /**
     * Updates an existing quote.
     * @param request The request that is handled
     * @param id The id of the quote to be patched
     * @param patch The pathc to be applied
     *
     * @return A page containing the updated resource.
     */
    @PATCH
    @Path("/:id")
    public SingleResultPage<QuickQuote> patchQuote(Request request, Long id, @Body QuotePatch patch) {
        final QuickQuote quote = dao.getEntityById(id)
                .orElseThrow(() -> new EntityNotFoundException(QuickQuote.class.getName(), id));

        permissionChecker.checkUpdateEntity(quote, getAuthenticatedUser(request));

        patch.note.ifPresent(quote::setNote);
        patch.text.ifPresent(quote::setText);
        patch.title.ifPresent(quote::setTitle);

        dao.updateEntity(quote);

        return new SingleResultPage<>(quote, getEntityURL(request));
    }
}
