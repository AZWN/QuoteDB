package nl.azwaan.quotedb.exceptions;

public class EntityNotFoundException extends RuntimeException {

    /**
     * @param type The type that was queried.
     * @param id The id that was queried for
     */
    public EntityNotFoundException(String type, Long id) {
        super(String.format("Entity with id %d not found in type %s", id, type));
    }
}
