package nl.azwaan.quotedb.exceptions;

public class InvalidSortingSpecificationException extends RuntimeException {
    /**
     * Creates new Exception to indicate sorting specification is invalid.
     * @param value The invalid sorting specification.
     */
    public InvalidSortingSpecificationException(String value) {
        super(String.format("Invalid sorting parameter: \"%s\". "
                + "Parameter should be in the format 'order_by=<field>.<asc|desc>'", value));
    }
}
