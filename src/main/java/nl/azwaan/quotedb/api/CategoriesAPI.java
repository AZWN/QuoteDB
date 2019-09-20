package nl.azwaan.quotedb.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import nl.azwaan.quotedb.dao.CategoriesDAO;
import nl.azwaan.quotedb.models.Category;

import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.Body;
import org.jooby.mvc.Consumes;
import org.jooby.mvc.GET;
import org.jooby.mvc.POST;
import org.jooby.mvc.Path;
import org.jooby.mvc.Produces;

import java.util.stream.Stream;

/**
 * MVC Component defining REST endpoints for manipulating {@link Category} resources.
 *
 * @author Aron Zwaan
 */
@Singleton
@Path("/categories")
@Produces("application/json")
@Consumes("application/json")
public class CategoriesAPI {

    private CategoriesDAO categoriesDAO;

    /**
     * Creates a new {@link Category} controller.
     * @param categoriesDAO The object used to access category resources.
     */
    @Inject
    public CategoriesAPI(CategoriesDAO categoriesDAO) {
        this.categoriesDAO = categoriesDAO;
    }

    /**
     * Returns all categories.
     * @return A {@link Stream} of all {@link Category} resources in the database.
     */
    @GET
    public Stream<Category> getAll() {
        return categoriesDAO.getAll();
    }

    /**
     * Adds a new {@link Category} resource.
     * @param category the category definition to add.
     * @return The result of the operation. Either a CREATED response with the resource, or a CONFLICT error.
     */
    @POST
    public Result addCategory(@Body Category category) {
        if (categoriesDAO.categoryWithNameExists(category.name)) {
            throw new ResourceConflictException("Category with name '%s' already exists", category.name);
        }

        final Category cat = categoriesDAO.createCategory(category.name);
        final Result res = Results.ok(cat);
        res.status(Status.CREATED);
        return res;
    }
}
