package nl.azwaan.quotedb.api.patches;

import java.util.Optional;

public class QuotePatch {
    public Optional<String> note = Optional.empty();
    public Optional<String> text = Optional.empty();
    public Optional<String> title = Optional.empty();
}
