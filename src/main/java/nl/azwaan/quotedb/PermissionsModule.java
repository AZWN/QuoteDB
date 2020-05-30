package nl.azwaan.quotedb;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.typesafe.config.Config;
import nl.azwaan.quotedb.models.Author;
import nl.azwaan.quotedb.models.Book;
import nl.azwaan.quotedb.models.BookQuote;
import nl.azwaan.quotedb.models.Label;
import nl.azwaan.quotedb.models.QuickQuote;
import nl.azwaan.quotedb.models.User;
import nl.azwaan.quotedb.permissions.PermissionChecker;
import nl.azwaan.quotedb.permissions.UserModelPermissionChecker;
import nl.azwaan.quotedb.permissions.UserPermissionChecker;
import org.jooby.Env;
import org.jooby.Jooby;

public class PermissionsModule implements Jooby.Module {
    @Override
    public void configure(Env env, Config conf, Binder binder) throws Throwable {
        binder.bind(new TypeLiteral<PermissionChecker<Author>>() { })
                .toInstance(new UserModelPermissionChecker<>());
        binder.bind(new TypeLiteral<PermissionChecker<Book>>() { })
                .toInstance(new UserModelPermissionChecker<>());
        binder.bind(new TypeLiteral<PermissionChecker<BookQuote>>() { })
                .toInstance(new UserModelPermissionChecker<>());
        binder.bind(new TypeLiteral<PermissionChecker<Label>>() { })
                .toInstance(new UserModelPermissionChecker<>());
        binder.bind(new TypeLiteral<PermissionChecker<QuickQuote>>() { })
                .toInstance(new UserModelPermissionChecker<>());
        binder.bind(new TypeLiteral<PermissionChecker<User>>() { })
                .toInstance(new UserPermissionChecker());
    }
}
