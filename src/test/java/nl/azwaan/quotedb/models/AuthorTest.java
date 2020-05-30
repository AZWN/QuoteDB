package nl.azwaan.quotedb.models;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class AuthorTest {

    @Test
    public void testFormatFullName() {
        Author author = new Author() {{
            setFirstName("Charles");
            setMiddleName("");
            setLastName("Dickens");
            setInitials("C. J. H.");
        }};

        String format = "{initials} ({firstName}) {middleName} {lastName}";

        assertThat(author.formatFullName(format), equalTo("C. J. H. (Charles) Dickens"));
    }
}
