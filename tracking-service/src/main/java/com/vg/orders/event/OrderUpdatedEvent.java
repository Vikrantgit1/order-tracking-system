package com.vg.orders.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderUpdatedEvent {
    private Long orderId;
    private String status;
    private Instant updateTime;
}
