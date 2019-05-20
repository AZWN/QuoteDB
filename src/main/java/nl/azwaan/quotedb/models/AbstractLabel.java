package nl.azwaan.quotedb.models;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;

@Entity
public abstract class AbstractLabel {

    @Key @Generated
    public Long id;

    @Column(unique = true)
    public String labelName;
}
