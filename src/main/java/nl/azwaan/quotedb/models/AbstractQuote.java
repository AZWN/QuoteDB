package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;

@Entity
public abstract class AbstractQuote {

    /**
     * AbstractQuote id in database
     */
    @Generated
    @Key
    public Long id;

    /**
     * AbstractQuote text
     */
    public String text;

    /**
     * AbstractQuote author
     */
    public String author;

    /**
     * AbstractQuote source
     */
    public String source;
}
