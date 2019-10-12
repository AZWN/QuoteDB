package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.exceptions.ResourceConflictException;
import nl.azwaan.quotedb.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.NoSuchElementException;

/**
 * Object allowing operations over {@link User}s.
 *
 * @author Aron Zwaan
 */
@Singleton
public class UsersDAO {

    private final EntityStore<Persistable, User> store;

    /**
     * Constructs new {@link UsersDAO}.
     * @param store The {@link EntityStore} used to access the database.
     */
    @Inject
    public UsersDAO(EntityStore<Persistable, User> store) {
        this.store = store;
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

        store.insert(newUser);
        store.refresh(newUser);

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

        final User user = store.select(User.class)
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
        return store.count(User.class)
                .where(User.USER_NAME.eq(userName))
                .limit(1)
                .get()
                .value() > 0;
    }

    /**
     * Returns the {@link User} with a given username.
     * @param userName The username to query
     * @return The associated User object
     */
    public User getUserByUserName(String userName) {
        return store.select(User.class)
                .where(User.USER_NAME.eq(userName))
                .get()
                .first();
    }

    /**
     * Fetches a user by id.
     * @param userId The id of the user to fetch.
     * @return The user with the given id.
     * @throws NoSuchElementException When the user with this id is not found.
     */
    public User getUserById(Long userId) {
        return store.select(User.class)
                .where(User.ID.eq(userId))
                .get()
                .first();
    }
}
