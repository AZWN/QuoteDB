package nl.azwaan.quotedb.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SingleResultPage<T> {

    /**
     * Creates a new page.
     * @param dataItem The item to be contained in the page.
     * @param link A link to the page.
     */
    public SingleResultPage(T dataItem, String link) {
        data = dataItem;
        metadata = new SingleResultPageMetaData(link);
    }

    private T data;

    private SingleResultPageMetaData metadata;

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static final class SingleResultPageMetaData {
        private String self;

        private SingleResultPageMetaData(String self) {
            this.self = self;
        }
    }
}
