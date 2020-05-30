package nl.azwaan.quotedb.api.patches;

import java.util.Optional;

public class BookPatch {
    public Optional<String> title = Optional.empty();
    public Optional<String> publisher = Optional.empty();
    public Optional<Integer> publicationYear = Optional.empty();
    public Optional<Long> author = Optional.empty();
}
