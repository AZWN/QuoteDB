package nl.azwaan.quotedb.exceptions;

public class PermissionDeniedException extends RuntimeException {

    /**
     * Constructs a new Exception with the given message.
     * @param message The exception message.
     */
    public PermissionDeniedException(String message) {
        super(message);
    }
}
