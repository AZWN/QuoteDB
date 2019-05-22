package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Index;
import io.requery.Key;

/**
 * All quotes are grouped in categories.
 *
 * @author Aron Zwaan
 */
@Entity
public abstract class AbstractCategory {

    /**
     * AbstractQuote id in database.
     */
    @Generated
    @Key
    public Long id;

    /**
     * Category name. TODO: make unique?
     */
    @Index
    public String name;
}
