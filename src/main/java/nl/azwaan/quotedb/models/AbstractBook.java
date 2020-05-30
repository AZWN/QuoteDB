package nl.azwaan.quotedb.models;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Index;
import io.requery.ManyToOne;

@Entity
public abstract class AbstractBook extends UserSpecificModel {

    @Column(nullable = false, index = true)
    @Index
    protected String title;

    @Column(nullable = false, index = true)
    @Index
    protected String publisher;

    protected int publicationYear;

    @ManyToOne
    protected Author author;

    @Override
    public void setUserOnSubfields(User user) {
        super.setUserOnSubfields(user);
        author.setUserOnSubfields(user);
    }
}
