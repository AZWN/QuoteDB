package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToMany;
import io.requery.JunctionTable;
import io.requery.PreInsert;

import java.util.Date;
import java.util.Set;

/**
 * Entity definition of Quotes.
 *
 * @author Aron Zwaan
 */
@Entity
public abstract class AbstractQuote {

    /**
     * AbstractQuote id in database.
     */
    @Generated
    @Key
    public Long id;

    /**
     * AbstractQuote text.
     */
    public String text;

    /**
     * AbstractQuote author.
     */
    public String author;

    /**
     * AbstractQuote source.
     */
    public String source;

    /**
     * Labels attached to this quote.
     */
    @ManyToMany
    @JunctionTable
    public Set<Label> labels;

    /**
     * The date this quote was saved.
     */
    public Date generationDate;

    /**
     * Before inserting the object, set the generationDate to the current {@link Date}.
     */
    @PreInsert
    public void setDate() {
        generationDate = new Date();
    }
}
