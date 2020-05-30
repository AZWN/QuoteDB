package nl.azwaan.quotedb.permissions;

import nl.azwaan.quotedb.exceptions.PermissionDeniedException;
import nl.azwaan.quotedb.models.User;

public interface PermissionChecker<T> {

    /**
     * Checks if a certain user may create an entity.
     * @param entity The entity the user wants to create.
     * @param user The user that wants to create an entity
     * @throws PermissionDeniedException When the permission is not granted.
     */
    void checkCreateEntity(T entity, User user);

    /**
     * Checks if a certain user may read an entity.
     * @param entity The entity the user wants to read.
     * @param user The user that wants to read an entity
     * @throws PermissionDeniedException When the permission is not granted.
     */
    void checkReadEntity(T entity, User user);

    /**
     * Checks if a certain user may update an entity.
     * @param entity The entity the user wants to update.
     * @param user The user that wants to update an entity
     * @throws PermissionDeniedException When the permission is not granted.
     */
    void checkUpdateEntity(T entity, User user);

    /**
     * Checks if a certain user may delete an entity.
     * @param entity The entity the user wants to delete.
     * @param user The user that wants to delete an entity
     * @throws PermissionDeniedException When the permission is not granted.
     */
    void checkDeleteEntity(T entity, User user);
}
