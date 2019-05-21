package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Index;
import io.requery.Key;

@Entity
public abstract class AbstractCategory {

    /**
     * AbstractQuote id in database
     */
    @Generated
    @Key
    public Long id;

    @Index
    public String name;
}
