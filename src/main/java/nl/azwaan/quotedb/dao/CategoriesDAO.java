package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.Category;

import java.util.stream.Stream;

/**
 * Class used to access the database for {@link Category} resources.
 *
 * @author Aron Zwaan
 */
@Singleton
public class CategoriesDAO {

    private EntityStore<Persistable, Category> categoryEntityStore;

    /**
     * Constructs a new {@link CategoriesDAO}.
     * @param categoryEntityStore The {@link EntityStore} used to access the database.
     */
    @Inject
    public CategoriesDAO(EntityStore<Persistable, Category> categoryEntityStore) {
        this.categoryEntityStore = categoryEntityStore;
    }

    /**
     * Returns all available categories.
     *
     * @return A {@link Stream} supplying all categories.
     */
    public Stream<Category> getAll() {
        return categoryEntityStore.select(Category.class)
                .get()
                .stream();
    }

    /**
     * Checks if a category with name categoryName exists.
     *
     * @param categoryName The name of the category to check existence of.
     * @return {@code true} if a category with the supplied name already exists, {@code false} otherwise.
     */
    public boolean categoryWithNameExists(String categoryName) {
        return categoryEntityStore.select(Category.class)
                .where(Category.NAME.eq(categoryName))
                .limit(1)
                .get()
                .stream()
                .findAny()
                .isPresent();
    }

    /**
     * Creates a new category with the given name. Does not check if the category does already exist.
     * @param name The name for the new category
     * @return The generated Category entity.
     */
    public Category createCategory(String name) {
        final Category cat = new Category();
        cat.setName(name);

        categoryEntityStore.insert(cat);
        categoryEntityStore.refresh(cat);

        return cat;
    }
}
