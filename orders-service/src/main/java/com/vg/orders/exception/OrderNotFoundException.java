package com.vg.orders.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id) {
        super("The order with id: " + id + " doesn't exist");
    }
}
