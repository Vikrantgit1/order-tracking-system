package com.vg.orders.service;

import com.vg.orders.exception.OrderNotFoundException;
import com.vg.orders.model.Orders;
import com.vg.orders.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, Object> createdKafkaTemplate;

    @Mock
    private KafkaTemplate<String, Object> updatedKafkaTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getOrderById_shouldReturnOrder_whenExists() {
        Orders order = new Orders();
        order.setId(1L);

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        ResponseEntity<Orders> response = orderService.fetchOrder(1L);
        Orders result = response.getBody();
        Assertions.assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getOrderById_shouldThrow_whenNotFound() {
        Mockito.when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.fetchOrder(99L));
    }

    @Test
    void createOrder_shouldReturnResponseEntityWith201() {
        Orders orderToCreate = new Orders();
        orderToCreate.setCustomerName("Test");
        orderToCreate.setItem("Item");

        Orders savedOrder = new Orders();
        savedOrder.setId(10L);
        savedOrder.setCustomerName("Test");
        savedOrder.setItem("Item");
        savedOrder.setStatus("PENDING");
        savedOrder.setCreatedAt(Instant.now());

        Mockito.when(orderRepository.save(Mockito.any(Orders.class))).thenReturn(savedOrder);

        ResponseEntity<Orders> response = orderService.createOrder(orderToCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());
    }

    @Test
    void updateOrderStatus_shouldReturnUpdatedEntity() {
        Orders existingOrder = new Orders();
        existingOrder.setId(1L);
        existingOrder.setStatus("PENDING");

        Orders updatedOrder = new Orders();
        updatedOrder.setId(1L);
        updatedOrder.setCustomerName(existingOrder.getCustomerName());
        updatedOrder.setItem(existingOrder.getItem());
        updatedOrder.setQuantity(existingOrder.getQuantity());
        updatedOrder.setStatus("SHIPPED");

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        Mockito.when(orderRepository.save(Mockito.any(Orders.class))).thenReturn(updatedOrder);

        ResponseEntity<Orders> response = orderService.updateOrderStatus(1L, "SHIPPED");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        assertEquals("SHIPPED", response.getBody().getStatus());
    }

    @Test
    void updateOrderStatus_shouldThrow_whenOrderNotFound() {
        Mockito.when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrderStatus(99L, "SHIPPED"));
    }
}