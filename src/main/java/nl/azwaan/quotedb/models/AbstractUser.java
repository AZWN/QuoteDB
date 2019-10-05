package nl.azwaan.quotedb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.requery.Entity;
import io.requery.Index;

@Entity
public abstract class AbstractUser extends BaseModel {

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
