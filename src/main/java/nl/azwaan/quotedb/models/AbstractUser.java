package nl.azwaan.quotedb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.requery.Column;
import io.requery.Entity;

@Entity
public abstract class AbstractUser extends BaseModel {

    /**
     * Username.
     */
    @Column(nullable = false, unique = true, index = true)
    protected String userName;

    /**
     * Bcrypt hashed password.
     */
    @JsonIgnore
    @Column(nullable = false)
    protected String password;
}
