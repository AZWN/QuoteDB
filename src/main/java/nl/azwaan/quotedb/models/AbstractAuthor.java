package nl.azwaan.quotedb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.requery.Column;
import io.requery.Entity;
import io.requery.Index;
import io.requery.Lazy;
import io.requery.OneToMany;

import java.util.Date;
import java.util.Set;

@Entity
public abstract class AbstractAuthor extends UserSpecificModel {

    @Column(nullable = false, index = true)
    @Index
    protected String firstName;

    @Column(nullable = false, index = true)
    @Index
    protected String lastName;

    @Column(nullable = false, value = "''")
    protected String middleName;

    @Column(nullable = false)
    protected String initials;

    @Column(nullable = false)
    protected Date dateOfBirth;

    @Column(definition = "TEXT")
    protected String biography;

    @OneToMany
    @Lazy
    @JsonIgnore
    protected Set<Book> books;

    /**
     * Returns the full name in the specified format.
     * @param format The format to put the name in.
     * @return The formatted full name.
     *
     * @implSpec <pre> {@code return format.replace("{firstName}", firstName)
     *                 .replace("{middleName}", middleName)
     *                 .replace("{lastName}", lastName)
     *                 .replace("{initials}", initials)
     *                 .replace("  ", " ")
     * }</pre>
     */
    public String formatFullName(String format) {
        return format.replace("{firstName}", firstName)
                .replace("{middleName}", middleName)
                .replace("{lastName}", lastName)
                .replace("{initials}", initials)
                .replace("  ", " ");
    }
}
