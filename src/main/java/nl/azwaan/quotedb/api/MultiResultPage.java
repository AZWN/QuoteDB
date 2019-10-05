package nl.azwaan.quotedb.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import net.moznion.uribuildertiny.URIBuilderTiny;

import java.net.URI;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MultiResultPage<T> {

    private List<T> data;

    private MultiResultPageMetadata metadata;

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static final class MultiResultPageMetadata {
        private int pageSize;
        private int totalResults;
        private MultiResultPageMetadataLinks links;

        private MultiResultPageMetadata(int pageSize, int totalResults, MultiResultPageMetadataLinks links) {
            this.pageSize = pageSize;
            this.totalResults = totalResults;
            this.links = links;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static final class MultiResultPageMetadataLinks {
        private String self;
        private String first;
        private String prev;
        private String next;
        private String last;

        private MultiResultPageMetadataLinks(String self, String first, String prev, String next, String last) {
            this.self = self;
            this.first = first;
            this.prev = prev;
            this.next = next;
            this.last = last;
        }
    }

    /**
     * Generates a MultiPageResult for the given results.
     * @param data The data to deliver in the page
     * @param totalResults The total number of results for the given query (without paging)
     * @param pageSize The number of results on this page
     * @param pageNumber The page number for this result
     * @param linkBase The basis link template
     * @param <T> The result entity type.
     * @return A page containing the results with all the given metadata.
     */
    public static <T> MultiResultPage<T> resultPageFor(List<T> data,
            int totalResults, int pageSize, int pageNumber, String linkBase)
    {
        final int residual = totalResults % pageSize;
        final int pageCount = totalResults / pageSize + (residual == 0 ? 0 : 1);

        final URI baseURI = new URIBuilderTiny(linkBase)
                .addQueryParameter("pageSize", pageSize)
                .build();

        final String selfURL = new URIBuilderTiny(baseURI).addQueryParameter("page", pageNumber).build().toString();
        final String firstURL = new URIBuilderTiny(baseURI).addQueryParameter("page", 1).build().toString();
        final String lastURL = new URIBuilderTiny(baseURI).addQueryParameter("page", pageCount).build().toString();
        final String prevURL = pageNumber > 1
                ? new URIBuilderTiny(baseURI).addQueryParameter("page", pageNumber - 1).build().toString()
                : null;
        final String nextURL = pageNumber < pageCount
                ? new URIBuilderTiny(baseURI).addQueryParameter("page", pageNumber + 1).build().toString()
                : null;

        final MultiResultPageMetadataLinks links =
                new MultiResultPageMetadataLinks(selfURL, firstURL, prevURL, nextURL, lastURL);

        final MultiResultPageMetadata metadata = new MultiResultPageMetadata(pageSize, totalResults, links);

        final MultiResultPage<T> result = new MultiResultPage<>();
        result.data = data;
        result.metadata = metadata;

        return result;
    }
}
