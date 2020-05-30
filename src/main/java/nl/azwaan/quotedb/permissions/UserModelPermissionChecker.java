package nl.azwaan.quotedb.permissions;

import nl.azwaan.quotedb.exceptions.PermissionDeniedException;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.models.UserSpecificModel;

public class UserModelPermissionChecker<T extends UserSpecificModel> implements PermissionChecker<T> {
    @Override
    public void checkCreateEntity(T entity, User user) {
        checkUserIsEntityOwner(entity, user);
    }

    @Override
    public void checkReadEntity(T entity, User user) {
        checkUserIsEntityOwner(entity, user);
    }

    @Override
    public void checkUpdateEntity(T entity, User user) {
        checkUserIsEntityOwner(entity, user);
    }

    @Override
    public void checkDeleteEntity(T entity, User user) {
        checkUserIsEntityOwner(entity, user);
    }

    protected void checkUserIsEntityOwner(T entity, User user) {
        checkUserIsEntityOwner(entity, user, "You are not the owner of the resource");
    }

    protected void checkUserIsEntityOwner(T entity, User user, String errorMessage) {
        if (!entity.getUser().equals(user)) {
            throw new PermissionDeniedException(errorMessage);
        }
    }
}
