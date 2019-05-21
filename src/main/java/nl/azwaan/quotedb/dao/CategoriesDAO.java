package nl.azwaan.quotedb.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.requery.EntityStore;
import io.requery.Persistable;
import nl.azwaan.quotedb.models.Category;

import java.util.stream.Stream;

@Singleton
public class CategoriesDAO {

    private EntityStore<Persistable, Category> categoryEntityStore;

    @Inject
    public CategoriesDAO(EntityStore<Persistable, Category> categoryEntityStore) {
        this.categoryEntityStore = categoryEntityStore;
    }

    public Stream<Category> getAll() {
        return categoryEntityStore.select(Category.class)
                .get()
                .stream();
    }

    public boolean categoryWithNameExists(String name) {
        return categoryEntityStore.select(Category.class)
                .where(Category.NAME.eq(name))
                .limit(1)
                .get()
                .stream()
                .findAny()
                .isPresent();
    }

    public Category createCategory(String name) {
        Category cat = new Category();
        cat.setName(name);

        categoryEntityStore.insert(cat);
        categoryEntityStore.refresh(cat);

        return cat;
    }
}
