package nl.azwaan.quotedb.models;

import io.requery.*;

import java.util.Date;
import java.util.Set;

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

    /**
     * Category to which this quote belongs
     */
    @ManyToOne
    public Category category;

    /**
     * Labels attached to this quote
     */
    @ManyToMany
    @JunctionTable
    public Set<Label> labels;

    public Date generationDate;

    @PreInsert
    public void setDate() {
        generationDate = new Date();
    }
}
