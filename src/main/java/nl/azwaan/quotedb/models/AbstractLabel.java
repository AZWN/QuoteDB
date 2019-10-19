package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Column;

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
    protected String labelName;

    /**
     * Label color
     */
    @Column(value = "'white'", nullable = false)
    protected String color;
}
