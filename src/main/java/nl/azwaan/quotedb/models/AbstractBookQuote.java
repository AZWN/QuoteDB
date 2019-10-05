package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.ManyToOne;

@Entity
public class AbstractBookQuote extends BaseQuote {

    @ManyToOne
    protected Book book;

    protected String pageRange;
}
