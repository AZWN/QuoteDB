package nl.azwaan.quotedb.models;

import io.requery.*;

@Entity
public abstract class AbstractLabel {

    @Key @Generated
    public Long id;

    @Index
    @Column(unique = true)
    public String labelName;
}
