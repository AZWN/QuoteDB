package nl.azwaan.quotedb.api;

import nl.azwaan.quotedb.api.querybuilding.BookQuoteFilterBuilder;
import nl.azwaan.quotedb.api.querybuilding.QuickQuoteFilterBuilder;
import nl.azwaan.quotedb.api.paging.MultiResultPage;
import nl.azwaan.quotedb.dao.BookQuotesDAO;
import nl.azwaan.quotedb.dao.QuotesDAO;
import nl.azwaan.quotedb.dao.UsersDAO;
import nl.azwaan.quotedb.models.BaseQuote;
import nl.azwaan.quotedb.models.BookQuote;
import nl.azwaan.quotedb.models.QuickQuote;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.users.UserIDProvider;
import org.jooby.Request;
import org.jooby.mvc.GET;
import org.jooby.mvc.Path;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("all")
public class AllQuotesAPI {

    private UsersDAO usersDAO;
    private QuotesDAO quotesDAO;
    private BookQuotesDAO bookQuotesDAO;

    private UserIDProvider userIDProvider;

    /**
     * Returns the joint result of all quotes.
     * @param request The request that is served
     * @return A page with the joint quote set.
     */
    @GET
    public MultiResultPage<BaseQuote> getAllQuotes(Request request) {
        final User authenticatedUser = getAuthenticatedUser(request);

        // Prepare quick quote stream
        final QuickQuoteFilterBuilder quickQuoteFilterBuilder =
                new QuickQuoteFilterBuilder(quotesDAO, authenticatedUser, request);
        final int quickQuotesCount = quickQuoteFilterBuilder.buildQuery(quotesDAO.countQuery(), false)
                .get().value();

        final BookQuoteFilterBuilder bookQuoteFilterBuilder =
                new BookQuoteFilterBuilder(bookQuotesDAO, authenticatedUser, request);
        final int bookQuotesCount = quickQuoteFilterBuilder.buildQuery(quotesDAO.countQuery(), false)
                .get().value();

        final int size = bookQuoteFilterBuilder.getResultCount();
        final int offset = bookQuoteFilterBuilder.getOffset();

        final Stream<QuickQuote> quoteStream = quickQuoteFilterBuilder.addFilter(quotesDAO.selectQuery())
                .limit(size)
                .get().stream();

        final Stream<BookQuote> bookQuoteStream = bookQuoteFilterBuilder.addFilter(bookQuotesDAO.selectQuery())
                .limit(size)
                .get().stream();

        try (Stream<? extends BaseQuote> allQuoteStream = Stream.concat(quoteStream, bookQuoteStream)
                .skip(offset)
                .limit(size)
                .onClose(quoteStream::close)
                .onClose(bookQuoteStream::close))
        {
            return MultiResultPage.resultPageFor(allQuoteStream.collect(Collectors.toList()),
                    quickQuotesCount + bookQuotesCount, size, offset / size, request.path());
        }
    }

    protected User getAuthenticatedUser(Request req) {
        final Long authenticatedUserId = userIDProvider.getUserId(req);
        return usersDAO.getUserById(authenticatedUserId);
    }
}
