package com.vg.orders.controller;

import com.vg.orders.model.Orders;
import com.vg.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<Orders> createOrder(@RequestBody Orders order){
        return orderService.createOrder(order);
    }

    @GetMapping("/fetchOrder/{id}")
    public ResponseEntity<Orders> fetchOrders(@PathVariable Long id){
        return orderService.fetchOrder(id);
    }

    @PutMapping("/updateOrderStatus/{id}")
    public ResponseEntity<Orders> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }
}
