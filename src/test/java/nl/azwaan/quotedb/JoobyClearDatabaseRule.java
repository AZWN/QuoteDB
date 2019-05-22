package nl.azwaan.quotedb;

import io.requery.EntityStore;
import io.requery.meta.Type;

import io.requery.query.Scalar;
import nl.azwaan.quotedb.models.Models;

import org.jooby.Jooby;
import org.jooby.test.JoobyRule;

public class JoobyClearDatabaseRule extends JoobyRule {
    private Jooby app;

    public JoobyClearDatabaseRule(Jooby app) {
        super(app);
        this.app = app;
    }

    protected void before() throws Throwable {
        super.before();

        EntityStore store = app.require(EntityStore.class);

        Models.DEFAULT.getTypes()
                .stream()
                .map(Type::getClassType)
                .forEach(t -> ((Scalar<Integer>) store.delete(t).get()).value());
    }
}
