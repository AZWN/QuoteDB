package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Column;
import io.requery.Index;

/**
 * Entity definition of a Label. Labels can be attached to Quote, to enable custom grouping.
 *
 * @author Aron Zwaan
 */
@Entity
public abstract class AbstractLabel extends UserSpecificModel {

    /**
     * Label name.
     */
    @Column(nullable = false, index = true)
    @Index
    protected String labelName;

    /**
     * Label color
     */
    @Column(value = "'white'", nullable = false)
    protected String color;
}
