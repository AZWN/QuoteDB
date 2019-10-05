package nl.azwaan.quotedb.integration.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.requery.EntityStore;
import io.requery.query.Result;
import io.restassured.http.ContentType;
import nl.azwaan.quotedb.models.Label;
import org.junit.Test;

public class LabelAPITests extends AuthenticatedTest {

    @Test
    public void testGetLabelsNoLabels() throws Throwable {

        getBase()
                .get("/api/labels")
                .then()
                .assertThat()
                .body(equalTo("[]"));
    }

    @Test
    public void testGetLabelsMultiple() throws Throwable {
        EntityStore store = app.require(EntityStore.class);

        Label label1 = new Label();
        label1.setLabelName("Label1");

        Label label2 = new Label();
        label2.setLabelName("Label2");

        store.insert(label1);
        store.insert(label2);


        getBase()
            .get("/api/labels")
            .then()
            .assertThat()
            .body("[0].labelName", equalTo("Label1"))
            .and().body("[1].labelName", equalTo("Label2"));

    }

    @Test
    public void testAddLabel() throws Throwable {
        EntityStore store = app.require(EntityStore.class);

        LabelParam param = new LabelParam();
        param.labelName = "Label3";

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

        long lblCount = ((Result<Label>) store.select(Label.class)
                .get())
                .stream()
                .count();

        assertThat(lblCount, equalTo(1L));
    }

    public class LabelParam {
        public String labelName;
    }
}
