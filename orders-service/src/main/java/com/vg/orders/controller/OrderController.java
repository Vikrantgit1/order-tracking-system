package com.vg.orders.controller;

import com.vg.orders.model.Orders;
import com.vg.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "APIs for managing orders")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/")
    @Operation(
        summary = "Create a new order",
        description = "Creates a new order and returns the created order details."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Order details to create",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = "{\"customerName\": \"Vikrant\", \"item\": \"Book\", \"quantity\": 1}"
                    )
            )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Order created successfully",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "400",
        description = "Invalid order details",
        content = @Content
    )
    public ResponseEntity<Orders> createOrder(@RequestBody Orders order){
        return orderService.createOrder(order);
    }

    @GetMapping("/fetchOrder/{id}")
    @Operation(
        summary = "Fetch an order by ID",
        description = "Retrieves the order details for the given order ID."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Order found",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "404",
        description = "Order not found",
        content = @Content
    )
    public ResponseEntity<Orders> fetchOrders(@PathVariable Long id){
        return orderService.fetchOrder(id);
    }

    @PutMapping("/updateOrderStatus/{id}")
    @Operation(
        summary = "Update order status",
        description = "Updates the status of the order with the given ID."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Order status updated successfully",
        content = @Content(mediaType = "application/json")
    )
    @ApiResponse(
        responseCode = "404",
        description = "Order not found",
        content = @Content
    )
    public ResponseEntity<Orders> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }
}
