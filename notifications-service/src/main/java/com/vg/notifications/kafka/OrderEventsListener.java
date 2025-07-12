package com.vg.notifications.kafka;

import com.vg.orders.event.OrderCreatedEvent;
import com.vg.orders.event.OrderUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventsListener {


    @KafkaListener(
            topics = "orders",
            groupId = "notification-service",
            containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    public void handleOrder(OrderCreatedEvent event){
        System.out.println("📨 EMAIL: Hi " + event.getCustomerName() + ", your order for " + event.getItem() + " has been received.");
    }

    @KafkaListener(
            topics = "order-status-update",
            groupId = "notification-service",
            containerFactory = "orderUpdatedKafkaListenerContainerFactory"
    )
    public void handleStatusUpdate(OrderUpdatedEvent event){
        System.out.println("📨 SMS: Order " + event.getOrderId() + " status changed to " + event.getStatus());
    }
}
