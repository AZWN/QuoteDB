package nl.azwaan.quotedb.models;

import io.requery.JunctionTable;
import io.requery.ManyToMany;
import io.requery.Superclass;

import java.util.Set;

@Superclass
public abstract class BaseQuote extends BaseModel {

    /**
     * Quote name/title.
     */
    protected String title;

    /**
     * Quote text.
     */
    public String text;

    /**
     * Optional note to add to the quote.
     */
    public String note;

    /**
     * Labels attached to this quote.
     */
    @ManyToMany
    @JunctionTable
    public Set<Label> labels;

}
