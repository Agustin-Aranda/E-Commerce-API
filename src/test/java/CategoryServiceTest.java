import com.revature.models.Category;
import com.revature.repos.interfaces.CategoryDAO;
import com.revature.services.CategoryService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    private CategoryDAO categoryDAO;
    private CategoryService categoryService;

    @Before
    public void setUp() {
        categoryDAO = mock(CategoryDAO.class);
        categoryService = new CategoryService(categoryDAO);
    }

    @Test
    public void testAllCategories() {
        Category category1 = new Category(1, "Electronics");
        Category category2 = new Category(2, "Clothing");
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryDAO.getAll()).thenReturn(categories);

        List<Category> result = categoryService.allCategories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Clothing", result.get(1).getName());
    }

    @Test
    public void testCategoryById() {
        Category category = new Category(1, "Electronics");

        when(categoryDAO.getById(1)).thenReturn(category);

        Category result = categoryService.categoryById(1);

        assertNotNull(result);
        assertEquals(1, result.getCategoryId());
        assertEquals("Electronics", result.getName());
    }

    @Test
    public void testUpdateCategory_Valid() {
        Category existingCategory = new Category(1, "Electronics");

        when(categoryDAO.getById(1)).thenReturn(existingCategory);
        when(categoryDAO.update(existingCategory)).thenReturn(existingCategory);

        Category result = categoryService.updateCategory(1, "Updated Category");

        assertNotNull(result);
        assertEquals("Updated Category", result.getName());
    }

    @Test
    public void testUpdateCategory_Invalid() {
        when(categoryDAO.getById(1)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.updateCategory(1, "Updated Category");
        });

        assertEquals("The category does not exist", exception.getMessage());
    }

    @Test
    public void testRegisterCategory() {
        Category categoryToBeSaved = new Category(1, "Electronics");

        when(categoryDAO.create(any(Category.class))).thenReturn(categoryToBeSaved);
        Category result = categoryService.registerCategory(1, "Electronics");
        assertNotNull(result);

        assertEquals(1, result.getCategoryId());
        assertEquals("Electronics", result.getName());

        verify(categoryDAO, times(1)).create(any(Category.class));
    }

    @Test
    public void testDeleteCategory() {
        when(categoryDAO.deleteById(1)).thenReturn(true);

        boolean result = categoryService.deleteCategory(1);

        assertTrue(result);
    }
}
