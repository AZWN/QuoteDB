package nl.azwaan.quotedb.integration.api;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.requery.EntityStore;
import io.requery.query.Result;
import io.restassured.http.ContentType;
import nl.azwaan.quotedb.models.Category;
import org.junit.Test;

import java.util.List;

public class CategoryAPITests extends AuthenticatedTest {

    @Test
    public void testGetAllCategories() {
        EntityStore store = app.require(EntityStore.class);

        Category cat1 = new Category();
        cat1.setName("Test category 1");

        store.insert(cat1);
        store.refresh(cat1);

        Category cat2 = new Category();
        cat2.setName("Test category 2");

        store.insert(cat2);
        store.refresh(cat2);

        getBase()
                .get("/api/categories")
                .then()
                .contentType(ContentType.JSON)
                .body("size()", equalTo(2)).and()
                .body("[0].name", equalTo("Test category 1")).and()
                .body("[1].name", equalTo("Test category 2"));
    }

    @Test
    public void testAddCategory() {
        postBase()
                .body("{\"name\":\"Cat 1\"}")
                .header("Authorization", getToken())
                .post("/api/categories")
                .then()
                .assertThat()
                .statusCode(201);

        EntityStore store = app.require(EntityStore.class);

        List<Category> categories = ((Result<Category>) store.select(Category.class)
                .get())
                .toList();

        assertThat(categories.size(), equalTo(1));

        Category cat = categories.get(0);
        assertThat(cat.name, equalTo("Cat 1"));
    }

    @Test
    public void testNoDoubleAddCategory() {
        postBase()
                .body("{\"name\":\"Cat 1\"}")
                .header("Authorization", getToken())
                .post("/api/categories")
                .then()
                .assertThat()
                .statusCode(201);

        postBase()
                .body("{\"name\":\"Cat 1\"}")
                .header("Authorization", getToken())
                .post("/api/categories")
                .then()
                .assertThat()
                .statusCode(409);

        EntityStore store = app.require(EntityStore.class);

        List<Category> categories = ((Result<Category>) store.select(Category.class)
                .get())
                .toList();

        assertThat(categories.size(), equalTo(1));

        Category cat = categories.get(0);
        assertThat(cat.name, equalTo("Cat 1"));
    }
}
