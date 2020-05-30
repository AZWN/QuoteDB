package nl.azwaan.quotedb.api.patches;

import java.util.Date;
import java.util.Optional;

public class AuthorPatch {
    public Optional<String> firstName = Optional.empty();
    public Optional<String> lastName = Optional.empty();
    public Optional<String> middleName = Optional.empty();
    public Optional<String> initials = Optional.empty();
    public Optional<Date> dateOfBirth = Optional.empty();
    public Optional<String> biography = Optional.empty();
}
