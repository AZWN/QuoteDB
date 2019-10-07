package nl.azwaan.quotedb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.requery.Column;
import io.requery.Entity;
import io.requery.Index;

@Entity
public abstract class AbstractUser extends BaseModel {

    /**
     * Username.
     */
    @Index
    @Column(nullable = false)
    protected String userName;

    /**
     * Bcrypt hashed password.
     */
    @JsonIgnore
    @Column(nullable = false)
    protected String password;
}
