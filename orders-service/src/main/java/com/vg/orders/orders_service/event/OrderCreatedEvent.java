package com.vg.orders.orders_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private String customerName;
    private String item;
    private Integer quantity;
    private String status;
    private Instant createdAt;
}
