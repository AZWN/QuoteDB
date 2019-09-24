package nl.azwaan.quotedb.data;

import io.requery.EntityStore;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.query.Selection;
import io.requery.query.WhereAndOr;
import nl.azwaan.quotedb.dao.UsersDAO;
import nl.azwaan.quotedb.models.User;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsersDAOTests {

    @Mock
    private EntityStore<Persistable, User> userEntityStore;

    @InjectMocks
    private UsersDAO usersDAO;

    @Test
    public void testFalseOnNullPassword() {
        userEntityStore = mock(EntityStore.class, RETURNS_DEEP_STUBS);
        usersDAO = new UsersDAO(userEntityStore);

        final String salt = BCrypt.gensalt();
        final String hashedPassword = BCrypt.hashpw("pwd1", salt);

        final User user = new User();
        user.setUserName("user1");
        user.setPassword(hashedPassword);

        final Selection s = mock(Selection.class);
        final WhereAndOr w = mock(WhereAndOr.class);
        final Result<User> r = mock(Result.class);
        final Stream<User> str = mock(Stream.class);

        // Set up password query
        when(userEntityStore.select(eq(User.class))).thenReturn(s);
        when(s.where(User.USER_NAME.eq("user1"))).thenReturn(w);
        when(w.get()).thenReturn(r);
        when(r.first()).thenReturn(user);

        // Set up username exists
        when(r.stream()).thenReturn(str);
        when(str.findAny()).thenReturn(Optional.of(user));

        Assert.assertThat(usersDAO.userHasPassword("user1", null), Is.is(false));
    }

}
