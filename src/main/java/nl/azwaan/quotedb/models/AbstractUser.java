package nl.azwaan.quotedb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Index;
import io.requery.Key;

@Entity
public class AbstractUser {

    /**
     * User id in database.
     */
    @Generated
    @Key
    public Long id;

    /**
     * Username.
     */
    @Index
    public String userName;

    /**
     * Bcrypt hashed password.
     */
    @JsonIgnore
    public String password;
}
