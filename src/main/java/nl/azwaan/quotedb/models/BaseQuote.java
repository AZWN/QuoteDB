package nl.azwaan.quotedb.models;

import io.requery.Column;
import io.requery.JunctionTable;
import io.requery.ManyToMany;
import io.requery.Superclass;

import java.util.Set;

@Superclass
public abstract class BaseQuote extends UserSpecificModel {

    /**
     * Quote name/title.
     */
    @Column(nullable = false, index = true)
    protected String title;

    /**
     * Quote text.
     */
    @Column(nullable = false, index = true)
    protected String text;

    /**
     * Optional note to add to the quote.
     */
    protected String note;

    /**
     * Labels attached to this quote.
     */
    @ManyToMany
    @JunctionTable
    protected Set<Label> labels;

}
