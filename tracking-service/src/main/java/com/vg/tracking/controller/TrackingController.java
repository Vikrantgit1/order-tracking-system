package com.vg.tracking.controller;

import com.vg.orders.event.OrderCreatedEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/track")
@Tag(name = "Tracking Controller", description = "APIs for tracking orders")
@RequiredArgsConstructor
public class TrackingController {

    private final RedisTemplate<String, OrderCreatedEvent> redis;

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Track an order by ID",
            description = "Retrieves the order status from Redis for the given order ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Order found",
            content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
            responseCode = "404",
            description = "Order not found in cache",
            content = @Content
    )
    public ResponseEntity<OrderCreatedEvent> trackOrder(@PathVariable Long orderId){
        System.out.println("Controller hit with id : "+orderId);
        OrderCreatedEvent event = redis.opsForValue().get("order:" + orderId);
        System.out.println("redis returned : "+event);
        return event!=null ? ResponseEntity.ok(event) : ResponseEntity.notFound().build();
    }
}
