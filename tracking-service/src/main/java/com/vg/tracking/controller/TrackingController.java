package com.vg.tracking.controller;

import com.vg.orders.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackingController {

    private final RedisTemplate<String, OrderCreatedEvent> redis;

    @GetMapping("/track/{orderId}")
    public ResponseEntity<OrderCreatedEvent> trackOrder(@PathVariable Long orderId){
        System.out.println("Controller hit with id : "+orderId);
        OrderCreatedEvent event = redis.opsForValue().get("order:" + orderId);
        System.out.println("redis returned : "+event);
        return event!=null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }
}
