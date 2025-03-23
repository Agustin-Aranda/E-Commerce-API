import com.revature.models.CartItem;
import com.revature.models.Product;
import com.revature.repos.interfaces.CartItemDAO;
import com.revature.repos.interfaces.ProductDAO;
import com.revature.services.CartItemService;
import com.revature.services.ProductService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartItemServiceTest {
    private CartItemDAO cartItemDAO;
    private ProductDAO productDAO;
    private CartItemService cartItemService;

    @Before
    public void setUp(){
        cartItemDAO = mock(CartItemDAO.class);
        productDAO = mock(ProductDAO.class);
        cartItemService = new CartItemService(productDAO, cartItemDAO);
    }

    @Test
    public void testAllCartItems() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem(1, 1, 1, 2),
                new CartItem(2, 1, 2, 3)
        );

        when(cartItemDAO.getAll()).thenReturn(cartItems);

        List<CartItem> result = cartItemService.allCartItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getUserId());
        assertEquals(2, result.get(1).getProductId());
    }

    @Test
    public void testCartItemById() {
        CartItem cartItem = new CartItem(1, 1, 1, 2);

        when(cartItemDAO.getById(1)).thenReturn(cartItem);

        CartItem result = cartItemService.cartItemById(1);

        assertNotNull(result);
        assertEquals(1, result.getCartItemId());
    }

    @Test
    public void testCartItemsByUserId() {
        List<CartItem> cartItems = Arrays.asList(
                new CartItem(1, 1, 1, 2),
                new CartItem(2, 1, 2, 3)
        );

        when(cartItemDAO.getCartItemsByUserId(1)).thenReturn(cartItems);

        List<CartItem> result = cartItemService.cartItemsByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testCartItemProduct() {
        CartItem cartItem = new CartItem(1, 1, 1, 2);

        when(cartItemDAO.getCartItemsByUserIdAndProductId(1, 1)).thenReturn(cartItem);

        CartItem result = cartItemService.cartItemProduct(1, 1);

        assertNotNull(result);
        assertEquals(1, result.getProductId());
    }

    @Test
    public void testRegisterCartItem_Valid() {
        BigDecimal a = new BigDecimal(12003.141);
        CartItem cartItem = new CartItem(1, 1, 1, 2);
        Product product = new Product(1, "Product1", "Description1", BigDecimal.valueOf(100), 10, 1);

        when(productDAO.getById(1)).thenReturn(product);
        when(cartItemDAO.getCartItemsByUserIdAndProductId(1, 1)).thenReturn(null);
        when(cartItemDAO.addToCart(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartItemService.registerCartItem(1, 1, 2);

        assertNotNull(result);
        assertEquals(1, result.getProductId());
        assertEquals(2, result.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterCartItem_ProductNotFound() {
        when(productDAO.getById(1)).thenReturn(null);
        cartItemService.registerCartItem(1, 1, 2);
    }

    @Test
    public void testDeleteFromCart() {
        CartItem cartItem = new CartItem(1, 1, 1, 2);

        when(cartItemDAO.getCartItemsByUserIdAndProductId(1, 1)).thenReturn(cartItem);
        when(cartItemDAO.removeFromCart(1, 1)).thenReturn(cartItem);

        CartItem result = cartItemService.DeleteFromCart(1, 1);

        assertNotNull(result);
        assertEquals(1, result.getProductId());
    }

    @Test
    public void testUpdateCartQuantityFromCardItem_Valid() {
        Product product = new Product(1, "Product1", "Description1", BigDecimal.valueOf(100), 10, 1);
        CartItem cartItem = new CartItem(1, 1, 1, 2);  // Cantidad inicial 2

        when(productDAO.getById(1)).thenReturn(product);
        when(cartItemDAO.getCartItemsByUserIdAndProductId(1, 1)).thenReturn(cartItem);
        when(cartItemDAO.updateCartQuantity(1, 1, 4)).thenReturn(new CartItem(1, 1, 1, 4));  // Devolver la cantidad actualizada

        CartItem result = cartItemService.updateCartQuantityFromCardItem(1, 1, 4);

        // Verificaciones
        assertNotNull(result);
        assertEquals(4, result.getQuantity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCartQuantityFromCardItem_StockInsufficient() {
        Product product = new Product(1, "Product1", "Description1", BigDecimal.valueOf(100), 3, 1);
        CartItem cartItem = new CartItem(1, 1, 1, 2);

        when(productDAO.getById(1)).thenReturn(product);
        when(cartItemDAO.getCartItemsByUserIdAndProductId(1, 1)).thenReturn(cartItem);

        cartItemService.updateCartQuantityFromCardItem(1, 1, 5);
    }
}
