package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.azwaan.quotedb.dao.CategoriesDAO;
import nl.azwaan.quotedb.models.Category;

import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.*;

import java.util.stream.Stream;

@Singleton
@Path("/categories")
@Produces("application/json")
@Consumes("application/json")
public class CategoriesAPI {

    private CategoriesDAO categoriesDAO;

    @Inject
    public CategoriesAPI(CategoriesDAO categoriesDAO) {
        this.categoriesDAO = categoriesDAO;
    }

    @GET
    public Stream<Category> getAll() {
        return categoriesDAO.getAll();
    }

    @POST
    public Result addCategory(@Body PostCategory category) {
        if (categoriesDAO.categoryWithNameExists(category.name)) {
            throw new ResourceConflictException("Category with name '%s' already exists", category.name);
        }

        Category cat = categoriesDAO.createCategory(category.name);
        Result res = Results.ok(cat);
        res.status(Status.CREATED);
        return res;
    }

    public static class PostCategory {
        public String name;
    }
}
