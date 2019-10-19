package nl.azwaan.quotedb.api.patches;

import java.util.List;
import java.util.Optional;

public class QuotePatch {
    public Optional<String> note = Optional.empty();
    public Optional<String> text = Optional.empty();
    public Optional<String> title = Optional.empty();
    public Optional<List<Long>> addLabels = Optional.empty();
    public Optional<List<Long>> removeLabels = Optional.empty();
}
