package nl.azwaan.quotedb.models;

import io.requery.Generated;
import io.requery.Key;
import io.requery.PreInsert;
import io.requery.PreUpdate;
import io.requery.Superclass;

import java.util.Date;

@Superclass
public abstract class BaseModel {

    /**
     * Unique identifier.
     */
    @Key
    @Generated
    protected Long id;

    /**
     * The date this entity was saved.
     */
    public Date generationDate;

    /**
     * Before inserting the object, set the generationDate to the current {@link Date}.
     */
    @PreInsert
    public void setGenerationDate() {
        generationDate = new Date();
        lastModifiedDate = new Date();
    }

    /**
     * The date this entity was last updated.
     */
    public Date lastModifiedDate;

    /**
     * Before updating the object, set lastModifiedDate to current date.
     */
    @PreUpdate
    public void setLastModifiedDate() {
        lastModifiedDate = new Date();
    }
}
