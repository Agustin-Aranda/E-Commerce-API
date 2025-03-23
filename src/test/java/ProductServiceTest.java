import com.revature.models.Product;
import com.revature.repos.interfaces.ProductDAO;
import com.revature.services.ProductService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    private ProductService productService;
    private ProductDAO productDAO;

    @Before
    public void setUp() {
        productDAO = mock(ProductDAO.class);
        productService = new ProductService(productDAO);
    }

    @Test
    public void testGetAllProducts() {
        Product product1 = new Product(1, "Product1", "Description1", BigDecimal.valueOf(100), 10, 1);
        Product product2 = new Product(2, "Product2", "Description2", BigDecimal.valueOf(200), 5, 2);
        when(productDAO.getAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product1", products.get(0).getName());
    }

    @Test
    public void testGetProductById() {
        Product product = new Product(1, "Product1", "Description1", BigDecimal.valueOf(100), 10, 1);
        when(productDAO.getById(1)).thenReturn(product);

        Product result = productService.getProductById(1);
        assertNotNull(result);
        assertEquals("Product1", result.getName());
    }

    @Test
    public void testGetProductByCategory() {
        Product product = new Product(1, "Product1", "Description1", BigDecimal.valueOf(100), 10, 1);
        when(productDAO.getByCategory(1)).thenReturn(Arrays.asList(product));

        List<Product> result = productService.getProductByCategory(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product1", result.get(0).getName());
    }

    @Test
    public void testRegisterNewProduct_ValidInput() {
        Product product = new Product("Product1", "Description1", BigDecimal.valueOf(100), 10, 1);
        when(productDAO.create(any(Product.class))).thenReturn(product);

        Product result = productService.registerNewProduct("Product1", "Description1", BigDecimal.valueOf(100), 10, 1);

        assertNotNull(result);
        assertEquals("Product1", result.getName());
    }

    @Test
    public void testRegisterNewProduct_InvalidStock() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.registerNewProduct("Product1", "Description1", BigDecimal.valueOf(100), 0, 1);
        });

        assertEquals("The stock can't be 0", exception.getMessage());
    }

    @Test
    public void testUpdateProduct_ValidInput() {
        Product existingProduct = new Product(1, "Product1", "Description1", BigDecimal.valueOf(100), 10, 1);
        when(productDAO.getById(1)).thenReturn(existingProduct);
        when(productDAO.update(any(Product.class))).thenReturn(existingProduct);

        Product result = productService.updateProduct(1, "UpdatedProduct", "UpdatedDescription", BigDecimal.valueOf(150), 15, 2);

        assertNotNull(result);
        assertEquals("UpdatedProduct", result.getName());
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productDAO.getById(1)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(1, "UpdatedProduct", "UpdatedDescription", BigDecimal.valueOf(150), 15, 2);
        });

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testDeleteProduct() {
        when(productDAO.deleteById(1)).thenReturn(true);
        boolean result = productService.deleteProduct(1);
        assertTrue(result);
    }

}
