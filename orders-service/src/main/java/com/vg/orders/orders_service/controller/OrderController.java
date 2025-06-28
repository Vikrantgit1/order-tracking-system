package com.vg.orders.orders_service.controller;

import com.vg.orders.orders_service.configuration.KafkaProducerConfig;
import com.vg.orders.orders_service.event.OrderCreatedEvent;
import com.vg.orders.orders_service.repository.OrderRepository;
import com.vg.orders.orders_service.model.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;

    @Autowired private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

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
        kafkaTemplate.send("orders", savedOrder.getId().toString(), event);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @GetMapping("/fetchOrder/{id}")
    public ResponseEntity<Orders> fetchOrders(@PathVariable Long id){
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
