package nl.azwaan.quotedb.exceptions;

/**
 * Exception thrown when a token does not contain all the necessary information.
 *
 * @author Aron Zwaan
 */
public class IncompleteTokenException extends RuntimeException {

    /**
     * Constructs a new {@link IncompleteTokenException}.
     * @param message String describing the error.
     */
    public IncompleteTokenException(String message) {
        super(message);
    }
}
