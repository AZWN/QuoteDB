package nl.azwaan.quotedb.exceptions;

public class EntityNotFoundException extends RuntimeException {

    /**
     * @param type The type that was queried.
     * @param id The id that was queried for
     */
    public EntityNotFoundException(String type, Long id) {
        super(String.format("Entity with id %d not found in type %s", id, type));
    }

    /**
     * @param type The type that was queried
     * @param key The key which was queried
     * @param value The value the should have had.
     */
    public EntityNotFoundException(String type, String key, String value) {
        super(String.format("Entity with %s %s not found in type %s", key, value, type));
    }
}
