package nl.azwaan.quotedb.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.dao.UsersDAO;
import nl.azwaan.quotedb.models.User;
import org.jooby.Request;

@Singleton
public class DevUserIDProvider implements UserIDProvider {

    private EntityStore<Persistable, User> userStore;
    private UsersDAO usersDAO;

    /**
     * Creates a {@link UserIDProvider} that gives a random id from the database, inserting a user if it does not exist.
     * @param userStore Used to query users.
     * @param usersDAO Used to insert user if it does not yet exists.
     */
    @Inject
    public DevUserIDProvider(EntityStore<Persistable, User> userStore, UsersDAO usersDAO) {
        this.userStore = userStore;
        this.usersDAO = usersDAO;
    }

    @Override
    public synchronized Long getUserId(Request request) {
        return userStore.count(User.class).get().value() > 0
                ? userStore.select(User.class).get().first().getId()
                : usersDAO.insertUser("user1", "pwd1").getId();
    }
}
