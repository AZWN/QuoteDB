package nl.azwaan.quotedb.permissions;

import nl.azwaan.quotedb.exceptions.PermissionDeniedException;
import nl.azwaan.quotedb.models.User;

public class UserPermissionChecker implements PermissionChecker<User> {
    @Override
    public void checkCreateEntity(User entity, User user) {
        // Creating a user is always allowed (even when not logged in)
    }

    @Override
    public void checkReadEntity(User entity, User user) {
        if (user != entity) {
            throw new PermissionDeniedException("May only read yourself");
        }
    }

    @Override
    public void checkUpdateEntity(User entity, User user) {
        if (user != entity) {
            throw new PermissionDeniedException("May only update yourself");
        }
    }

    @Override
    public void checkDeleteEntity(User entity, User user) {
        throw new PermissionDeniedException("A user may not be deleted");
    }
}
