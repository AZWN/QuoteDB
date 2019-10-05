package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;

import java.util.Date;

@Entity
public class AbstractBook {

    /**
     * Unique author identifier.
     */
    @Key
    protected Long id;

    protected String title;

    protected String publisher;

    protected Date publicationDate;

    @ManyToOne
    protected Author author;
}
