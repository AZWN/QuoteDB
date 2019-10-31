package nl.azwaan.quotedb.models;

import io.requery.Column;
import io.requery.Index;
import io.requery.JunctionTable;
import io.requery.Lazy;
import io.requery.ManyToMany;
import io.requery.Superclass;

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
    @Column(nullable = false, index = true)
    @Index
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
    @Lazy
    protected Set<Label> labels;

    /**
     * Updates title.
     * @param title The new title.
     */
    public abstract void setTitle(String title);

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

    /**
     * Gets the labels of this quote.
     * @return The labels of this quote.
     */
    public abstract Set<Label> getLabels();

    @Override
    public void setUserOnSubfields(User user) {
        super.setUserOnSubfields(user);
        getLabels().forEach(lbl -> lbl.setUser(user));
    }
}
