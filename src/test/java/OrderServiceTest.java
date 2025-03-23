import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.revature.models.Order;
import com.revature.models.OrderStatus;
import com.revature.repos.interfaces.OrderDAO;
import com.revature.services.OrderService;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class OrderServiceTest {

    private OrderService orderService;
    private OrderDAO orderDAO;

    @Before
    public void setUp() {
        orderDAO = mock(OrderDAO.class);
        orderService = new OrderService(orderDAO);
    }

    @Test
    public void testGetOrderByStatus() {
        Order order1 = new Order(1, 1, 100.0, OrderStatus.PENDING, LocalDateTime.now());
        Order order2 = new Order(2, 2, 150.0, OrderStatus.PENDING, LocalDateTime.now());
        when(orderDAO.getByStatus(OrderStatus.PENDING)).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getOrderByStatus(OrderStatus.PENDING);

        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals(OrderStatus.PENDING, orders.get(0).getStatus());
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order(1, 1, 100.0, OrderStatus.PENDING, LocalDateTime.now());
        when(orderDAO.getById(1)).thenReturn(order);

        Order result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(1, result.getOrderId());
    }

    @Test
    public void testGetOrderByUserId() {
        Order order1 = new Order(1, 1, 100.0, OrderStatus.PENDING, LocalDateTime.now());
        Order order2 = new Order(2, 1, 150.0, OrderStatus.SHIPPED, LocalDateTime.now());
        when(orderDAO.getByUserId(1)).thenReturn(Arrays.asList(order1, order2));

        List<Order> result = orderService.getOrderByUserId(1);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getUserId());
    }

    @Test
    public void testGetAllOrders() {
        Order order1 = new Order(1, 1, 100.0, OrderStatus.PENDING, LocalDateTime.now());
        Order order2 = new Order(2, 2, 150.0, OrderStatus.SHIPPED, LocalDateTime.now());
        when(orderDAO.getAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdateStatusOrder_Valid() {
        Order order = new Order(1, 1, 100.0, OrderStatus.PENDING, LocalDateTime.now());
        when(orderDAO.getById(1)).thenReturn(order);
        when(orderDAO.updateStatus(order, OrderStatus.SHIPPED)).thenReturn(order);

        Order result = orderService.updateStatusOrder(1, OrderStatus.SHIPPED);

        assertNotNull(result);
        assertEquals(OrderStatus.SHIPPED, result.getStatus());
    }

    @Test
    public void testUpdateStatusOrder_OrderNotFound() {
        when(orderDAO.getById(1)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateStatusOrder(1, OrderStatus.SHIPPED);
        });

        assertEquals("The order doesn't exist", exception.getMessage());
    }

    @Test
    public void testUpdateStatusOrder_UpdateFailed() {
        Order order = new Order(1, 1, 100.0, OrderStatus.PENDING, LocalDateTime.now());
        when(orderDAO.getById(1)).thenReturn(order);
        when(orderDAO.updateStatus(order, OrderStatus.SHIPPED)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.updateStatusOrder(1, OrderStatus.SHIPPED);
        });

        assertEquals("Failed to update order status.", exception.getMessage());
    }

    @Test
    public void testRegisterOrder() {
        Order order = new Order(1, 1, 100.0, OrderStatus.PENDING, LocalDateTime.now());
        when(orderDAO.create(any(Order.class))).thenReturn(order);

        Order result = orderService.registerOrder(1, 1, 100.0, OrderStatus.PENDING, LocalDateTime.now());

        assertNotNull(result);
        assertEquals(1, result.getOrderId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
    }

    @Test
    public void testDeleteOrder() {
        when(orderDAO.deleteById(1)).thenReturn(true);
        boolean result = orderService.deletOrder(1);
        assertTrue(result);
    }


}
