package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Index;
import io.requery.ManyToOne;

import java.util.Date;

@Entity
public abstract class AbstractBook extends BaseModel {

    @Index
    protected String title;

    protected String publisher;

    protected Date publicationDate;

    @ManyToOne
    protected Author author;
}
