package com.vg.orders.controller;

import com.vg.orders.event.OrderCreatedEvent;
import com.vg.orders.event.OrderUpdatedEvent;
import com.vg.orders.repository.OrderRepository;
import com.vg.orders.model.Orders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> createdKafkaTemplate;
    private final KafkaTemplate<String, Object> updatedKafkaTemplate;

    public OrderController(OrderRepository orderRepository,
                           @Qualifier("orderCreatedTemplate") KafkaTemplate<String, Object> createdKafkaTemplate,
                           @Qualifier("orderUpdatedTemplate") KafkaTemplate<String, Object> updatedKafkaTemplate) {
        this.orderRepository = orderRepository;
        this.createdKafkaTemplate = createdKafkaTemplate;
        this.updatedKafkaTemplate = updatedKafkaTemplate;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders order){
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

    @GetMapping("/fetchOrder/{id}")
    public ResponseEntity<Orders> fetchOrders(@PathVariable Long id){
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateOrderStatus/{id}")
    public ResponseEntity<Orders> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status)
    {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if(optionalOrders.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Orders updatedOrder = optionalOrders.get();
        updatedOrder.setStatus(status);
        orderRepository.save(updatedOrder);

        //Publish event to Kafka
        OrderUpdatedEvent orderUpdatedEvent = new OrderUpdatedEvent(id, status, Instant.now());
        updatedKafkaTemplate.send("order-status-update", updatedOrder.getId().toString(), orderUpdatedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(updatedOrder);
    }
}
