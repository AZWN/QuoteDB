package nl.azwaan.quotedb.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.requery.Column;
import io.requery.ManyToOne;
import io.requery.Superclass;

@Superclass
public abstract class UserSpecificModel extends BaseModel {

    @Column(index = true, nullable = false, name = "owner")
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    protected User user;

    /**
     * Sets the user for this entity.
     * @param user The user to attach to this entity.
     */
    public abstract void setUser(User user);

    /**
     * Gets the user for this entity.
     * @return The user for this entity.
     */
    public abstract User getUser();

    /**
     * Set the user property for this entry, but also on all subfields.
     * @param user The user to set.
     */
    public void setUserOnSubfields(User user) {
        setUser(user);
    }
}
