package nl.azwaan.quotedb.exceptions;

/**
 * Exception to be thrown when an invalid request is made.
 *
 * @author Aron Zwaan
 */
public class InvalidRequestException extends RuntimeException {

    /**
     * Instantiates a new Invalid Request Exception.
     * @param message The message to be displayed to the user.
     */
    public InvalidRequestException(String message) {
        super(message);
    }
}
