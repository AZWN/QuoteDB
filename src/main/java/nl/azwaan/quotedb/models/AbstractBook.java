package nl.azwaan.quotedb.models;

import io.requery.Column;
import io.requery.Entity;
import io.requery.ManyToOne;

import java.util.Date;

@Entity
public abstract class AbstractBook extends UserSpecificModel {

    @Column(nullable = false, index = true)
    protected String title;

    @Column(nullable = false)
    protected String publisher;

    protected Date publicationDate;

    @ManyToOne
    protected Author author;
}
