import com.revature.models.OrderItem;
import com.revature.repos.interfaces.OrderItemDAO;
import com.revature.services.OrderItemService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderItemServiceTest {

    private OrderItemDAO orderItemDAO;
    private OrderItemService orderItemService;

    @Before
    public void setUp() {
        orderItemDAO = mock(OrderItemDAO.class);
        orderItemService = new OrderItemService(orderItemDAO);
    }

    @Test
    public void testAllOrderItems() {
        BigDecimal a = new BigDecimal(231.145);
        BigDecimal b = new BigDecimal(123.123);

        OrderItem orderItem1 = new OrderItem(1, 1, 1, 10, a);
        OrderItem orderItem2 = new OrderItem(2, 1, 5, 2,  b);
        List<OrderItem> mockOrderItems = Arrays.asList(orderItem1, orderItem2);

        when(orderItemDAO.getAll()).thenReturn(mockOrderItems);

        List<OrderItem> result = orderItemService.allOrderItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(orderItem1, result.get(0));
        assertEquals(orderItem2, result.get(1));
    }

    @Test
    public void testOrderItemById_Valid() {
        BigDecimal a = new BigDecimal(100.921);
        OrderItem orderItem = new OrderItem(1, 1, 3, 5, a);

        when(orderItemDAO.getById(1)).thenReturn(orderItem);

        OrderItem result = orderItemService.orderItemById(1);

        assertNotNull(result);
        assertEquals(1, result.getOrderId());
        assertEquals(5, result.getQuantity());
        assertEquals(a, result.getPrice());
    }

    @Test
    public void testOrderItemById_NotFound() {
        when(orderItemDAO.getById(1)).thenReturn(null);

        OrderItem result = orderItemService.orderItemById(1);

        assertNull(result);
    }

    @Test
    public void testDeleterOrderItemById_Valid() {
        when(orderItemDAO.deleteById(1)).thenReturn(true);
        boolean result = orderItemService.deleterOrderItemById(1);
        assertTrue(result);
    }

    @Test
    public void testDeleterOrderItemById_Invalid() {
        when(orderItemDAO.deleteById(1)).thenReturn(false);
        boolean result = orderItemService.deleterOrderItemById(1);
        assertFalse(result);
    }

    @Test
    public void testRegisterOrder() {
        BigDecimal a = new BigDecimal(45.54);
        BigDecimal b = new BigDecimal(898.90);
        OrderItem orderItem1 = new OrderItem(1, 1, 2, 10, a);
        OrderItem orderItem2 = new OrderItem(2, 1, 5, 6, b);
        List<OrderItem> mockOrderItems = Arrays.asList(orderItem1, orderItem2);

        when(orderItemDAO.placeOrder(1)).thenReturn(mockOrderItems);

        List<OrderItem> result = orderItemService.registerOrder(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(orderItem1, result.get(0));
        assertEquals(orderItem2, result.get(1));
    }
}
