package nl.azwaan.quotedb.models;

import io.requery.Key;
import io.requery.Generated;
import io.requery.Entity;
import io.requery.Index;
import io.requery.Column;

/**
 * Entity definition of a Label. Labels can be attached to Quote, to enable custom grouping.
 *
 * @author Aron Zwaan
 */
@Entity
public abstract class AbstractLabel {

    /**
     * Label id.
     */
    @Key @Generated
    public Long id;

    /**
     * Label name. Is unique, which means a label can never be inserted twice.
     */
    @Index
    @Column(unique = true)
    public String labelName;
}
