package nl.azwaan.quotedb.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.requery.Column;
import io.requery.Entity;
import io.requery.ManyToOne;

@Entity
public abstract class AbstractBook extends UserSpecificModel {

    @Column(nullable = false, index = true)
    protected String title;

    @Column(nullable = false)
    protected String publisher;

    protected int publicationYear;

    @ManyToOne
    @JsonManagedReference
    protected Author author;
}
