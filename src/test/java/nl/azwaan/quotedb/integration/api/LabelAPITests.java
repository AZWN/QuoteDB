package nl.azwaan.quotedb.integration.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.requery.EntityStore;
import io.requery.query.Result;
import io.requery.query.Scalar;
import io.restassured.http.ContentType;
import nl.azwaan.quotedb.models.Label;
import nl.azwaan.quotedb.models.User;
import org.junit.Test;

public class LabelAPITests extends AuthenticatedTest {

    @Test
    public void testGetLabelsNoLabels() throws Throwable {

        getBase()
                .get("/api/labels")
                .then()
                .assertThat()
                .body("data.size()", equalTo(0));
    }

    @Test
    public void testGetLabelsMultiple() throws Throwable {
        EntityStore store = app.require(EntityStore.class);
        User user = getUser();

        Label label1 = new Label();
        label1.setLabelName("Label1");
        label1.setUser(user);

        Label label2 = new Label();
        label2.setLabelName("Label2");
        label2.setUser(user);

        store.insert(label1);
        store.insert(label2);

        getBase()
            .get("/api/labels")
            .then()
            .assertThat()
            .body("data[0].labelName", equalTo("Label1"))
            .and().body("data[1].labelName", equalTo("Label2"));

    }

    @Test
    public void testAddLabel() throws Throwable {
        EntityStore store = app.require(EntityStore.class);

        Label param = new Label();
        param.setLabelName("Label3");

        postBase()
                .body(param)
                .post("/api/labels")
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(201);

        Label lbl = ((Result<Label>) store.select(Label.class)
                .get())
                .stream()
                .findFirst()
                .get();

        assertThat(lbl.getLabelName(), equalTo("Label3"));
    }

    @Test
    public void testNoDoubleLabel() throws Throwable {
        EntityStore store = app.require(EntityStore.class);

        LabelParam param = new LabelParam();
        param.labelName = "Label4";

        postBase()
                .body(param)
                .post("/api/labels")
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .statusCode(201);

        // Insert same label again
        postBase()
                .body(param)
                .post("/api/labels")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(409);

        int lblCount = ((Scalar<Integer>) store.count(Label.class)
                .get())
                .value();

        assertThat(lblCount, equalTo(1));
    }

    public class LabelParam {
        public String labelName;
    }
}
