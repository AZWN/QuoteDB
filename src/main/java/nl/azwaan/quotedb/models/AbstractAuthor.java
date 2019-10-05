package nl.azwaan.quotedb.models;

import io.requery.Entity;
import io.requery.Key;

import java.util.Date;

@Entity
public abstract class AbstractAuthor {

    /**
     * Unique author identifier.
     */
    @Key
    protected Long id;

    protected String firstName;

    protected String lastName;

    protected String middleName;

    protected String initials;

    protected Date dateOfBirth;

    protected String biography;

    /**
     * Returns the full name in the specified format.
     * @param format The format to put the name in.
     * @return The formatted full name.
     *
     * @implSpec <pre> {@code return format.replace("{firstName}", firstName)
     *                 .replace("{middleName}", middleName)
     *                 .replace("{lastName}", lastName)
     *                 .replace("{initials}", initials)
     * }</pre>
     */
    public String formatFullName(String format) {
        return format.replace("{firstName}", firstName)
                .replace("{middleName}", middleName)
                .replace("{lastName}", lastName)
                .replace("{initials}", initials);
    }
}
