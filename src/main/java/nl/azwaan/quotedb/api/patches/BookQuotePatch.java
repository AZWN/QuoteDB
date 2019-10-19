package nl.azwaan.quotedb.api.patches;

import java.util.Optional;

public class BookQuotePatch extends QuotePatch {
    public Optional<Long> book = Optional.empty();
    public Optional<String> pageRange = Optional.empty();
}
