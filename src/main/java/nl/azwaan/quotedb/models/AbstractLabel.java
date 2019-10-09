package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Column;

/**
 * Entity definition of a Label. Labels can be attached to Quote, to enable custom grouping.
 *
 * @author Aron Zwaan
 */
@Entity
public abstract class AbstractLabel extends BaseModel {

    /**
     * Label name. Is unique, which means a label can never be inserted twice.
     */
    @Column(unique = true, nullable = false, index = true)
    protected String labelName;
}
