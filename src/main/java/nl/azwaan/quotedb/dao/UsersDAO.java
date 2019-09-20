package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.api.ResourceConflictException;
import nl.azwaan.quotedb.models.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Object allowing operations over {@link User}s.
 *
 * @author Aron Zwaan
 */
@Singleton
public class UsersDAO {

    private EntityStore<Persistable, User> usersEntityStore;

    /**
     * Constructs new {@link UsersDAO}.
     * @param usersEntityStore The {@link EntityStore} used to access the database.
     */
    @Inject
    public UsersDAO(EntityStore<Persistable, User> usersEntityStore) {
        this.usersEntityStore = usersEntityStore;
    }

    /**
     * Creates a new {@link User}.
     * @param userName The username of the new user, may not exist yet
     * @param password The password of the new User
     * @return The new User object
     */
    public User insertUser(String userName, String password) {
        if (userNameExists(userName)) {
            throw new ResourceConflictException("This username is already taken");
        }

        final String salt = BCrypt.gensalt();
        final String hashedPassword = BCrypt.hashpw(password, salt);

        final User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassword(hashedPassword);

        usersEntityStore.insert(newUser);
        usersEntityStore.refresh(newUser);

        return newUser;
    }

    /**
     * Returns true is the provided password is the user password, false otherwise.
     * @param userName The username to validate the password for
     * @param password The password to validate
     * @return Whether the password is correct
     */
    public boolean userHasPassword(String userName, String password) {
        if (!userNameExists(userName)) {
            return false;
        }
        final User user = usersEntityStore.select(User.class)
                .where(User.USER_NAME.eq(userName))
                .get()
                .first();

        return BCrypt.checkpw(password, user.getPassword());
    }

    /**
     * Checks whether a given username is in use.
     * @param userName The username to check
     * @return true is the username is in use, false otherwise.
     */
    public boolean userNameExists(String userName) {
        return usersEntityStore.select(User.class)
                .where(User.USER_NAME.eq(userName))
                .get()
                .stream()
                .findAny()
                .isPresent();
    }

    /**
     * Returns the {@link User} with a given username.
     * @param userName The username to query
     * @return The associated User object
     */
    public User getUserByUserName(String userName) {
        return usersEntityStore.select(User.class)
                .where(User.USER_NAME.eq(userName))
                .get()
                .first();
    }
}
