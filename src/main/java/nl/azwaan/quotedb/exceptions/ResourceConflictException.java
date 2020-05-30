package nl.azwaan.quotedb.exceptions;

/**
 * Exception thrown when a resource can not be created
 * due to the fact another resource with the same key already exists.
 *
 * @author Aron Zwaan
 */
public class ResourceConflictException extends RuntimeException {

    /**
     * Constructs a new {@link ResourceConflictException}, formatting the given message.
     *
     * @param message Message format
     * @param params Format parameters
     */
    public ResourceConflictException(String message, Object... params) {
        super(String.format(message, params));
    }
}
