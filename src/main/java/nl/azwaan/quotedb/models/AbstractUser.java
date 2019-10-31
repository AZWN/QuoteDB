package nl.azwaan.quotedb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.requery.Column;
import io.requery.Entity;
import io.requery.Index;
import io.requery.Table;

@Entity
@Table(name = "QuoteDBUser")
public abstract class AbstractUser extends BaseModel {

    /**
     * Username.
     */
    @Column(nullable = false, unique = true, index = true)
    @Index
    protected String userName;

    /**
     * Bcrypt hashed password.
     */
    @JsonIgnore
    @Column(nullable = false)
    protected String password;
}
