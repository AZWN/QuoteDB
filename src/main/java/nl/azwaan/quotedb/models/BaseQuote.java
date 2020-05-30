package nl.azwaan.quotedb.models;

import io.requery.Column;
import io.requery.Index;
import io.requery.JunctionTable;
import io.requery.Lazy;
import io.requery.ManyToMany;
import io.requery.Superclass;
import io.requery.Transient;

import java.util.Set;

@Superclass
public abstract class BaseQuote extends UserSpecificModel {

    /**
     * Quote name/title.
     */
    @Column(nullable = false, index = true)
    @Index
    protected String title;

    /**
     * Quote text.
     */
    @Column(nullable = false, index = true, definition = "VARCHAR (8000)")
    @Index
    protected String text;

    /**
     * Optional note to add to the quote.
     */
    @Column(definition = "VARCHAR (2000)")
    protected String note;

    /**
     * Labels attached to this quote.
     */
    @ManyToMany
    @JunctionTable
    @Lazy
    protected Set<Label> labels;

    @Transient
    protected String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Updates title.
     * @param title The new title.
     */
    public abstract void setTitle(String title);

    /**
     * Gets the labels of this quote.
     * @return The labels of this quote.
     */
    public abstract Set<Label> getLabels();

    /**
     * Updates text.
     * @param text The new title.
     */
    public abstract void setText(String text);

    /**
     * Updates note.
     * @param note The new note.
     */
    public abstract void setNote(String note);

    @Override
    public void setUserOnSubfields(User user) {
        super.setUserOnSubfields(user);
        getLabels().forEach(lbl -> lbl.setUser(user));
    }
}
