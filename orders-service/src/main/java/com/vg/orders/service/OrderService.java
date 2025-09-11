package com.vg.orders.service;

import com.vg.orders.event.OrderCreatedEvent;
import com.vg.orders.event.OrderUpdatedEvent;
import com.vg.orders.exception.OrderNotFoundException;
import com.vg.orders.model.Orders;
import com.vg.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> createdKafkaTemplate;
    private final KafkaTemplate<String, Object> updatedKafkaTemplate;

    public OrderService(OrderRepository orderRepository,
                        @Qualifier("orderCreatedTemplate") KafkaTemplate<String, Object> createdKafkaTemplate,
                        @Qualifier("orderUpdatedTemplate") KafkaTemplate<String, Object> updatedKafkaTemplate) {
        this.orderRepository = orderRepository;
        this.createdKafkaTemplate = createdKafkaTemplate;
        this.updatedKafkaTemplate = updatedKafkaTemplate;
    }


    public ResponseEntity<Orders> createOrder(Orders order) {
        if (order.getCustomerName() == null || order.getItem() == null) {
            throw new IllegalArgumentException("Customer name and item are required.");
        }

        order.setStatus("PENDING");
        order.setCreatedAt(Instant.now());
        Orders savedOrder = orderRepository.save(order);

        // Publish event to Kafka
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(), savedOrder.getCustomerName(), savedOrder.getItem(),
                savedOrder.getQuantity(), savedOrder.getStatus(), savedOrder.getCreatedAt()
        );
        createdKafkaTemplate.send("orders", savedOrder.getId().toString(), event);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    public ResponseEntity<Orders> fetchOrder(Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public ResponseEntity<Orders> updateOrderStatus(Long id, String status) {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if(optionalOrders.isEmpty()){
            throw new OrderNotFoundException(id);
        }
        Orders updatedOrder = optionalOrders.get();
        updatedOrder.setStatus(status);
        orderRepository.save(updatedOrder);

        //Publish event to Kafka
        OrderUpdatedEvent orderUpdatedEvent = new OrderUpdatedEvent(id, status, Instant.now());
        updatedKafkaTemplate.send("order-status-update", updatedOrder.getId().toString(), orderUpdatedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);
    }

    public List<Orders> fetchAllOrders(Long id, String customerName, String item, String status) {
        return orderRepository.findAllWithFilters(
                id, customerName, item, status
        );
    }
}
