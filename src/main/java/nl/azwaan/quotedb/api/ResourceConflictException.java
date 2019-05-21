package nl.azwaan.quotedb.api;

public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message, Object... params) {
        super(String.format(message, params));
    }
}
