package nl.azwaan.quotedb.models;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Lazy;
import io.requery.OneToMany;

import java.util.Date;
import java.util.Set;

@Entity
public abstract class AbstractAuthor extends UserSpecificModel {

    @Column(nullable = false)
    protected String firstName;

    @Column(nullable = false)
    protected String lastName;

    @Column(nullable = false)
    protected String middleName;

    @Column(nullable = false)
    protected String initials;

    @Column(nullable = false)
    protected Date dateOfBirth;

    protected String biography;

    @OneToMany
    @Lazy
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
