package com.vg.tracking.kafka;

import com.vg.orders.event.OrderCreatedEvent;
import com.vg.orders.event.OrderUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OrderEventsListener {

    private final RedisTemplate<String, OrderCreatedEvent> redis;

    @KafkaListener(topics = "orders", groupId = "tracking-service", containerFactory = "orderCreatedKafkaListenerContainerFactory")
    public void handleOrder(OrderCreatedEvent event){
        System.out.println("📩 Received event: " + event);
        redis.opsForValue().set("order:" + event.getOrderId(), event, Duration.ofMinutes(10));
    }

    @KafkaListener(topics = "order-status-update", groupId = "tracking-service", containerFactory = "orderUpdatedKafkaListenerContainerFactory")
    public void handleStatusUpdate(OrderUpdatedEvent event){
        System.out.println("🔁 Status updated: " + event);

        String key = "order:"+event.getOrderId();
        OrderCreatedEvent cached = redis.opsForValue().get(key);

        if (cached != null) {
            cached.setStatus(event.getStatus());
            redis.opsForValue().set(key, cached, Duration.ofMinutes(10));
            System.out.println("✅ Redis updated with new status: " + event.getStatus());
        } else {
            System.out.println("⚠️ Order not found in Redis for ID: " + event.getOrderId());
        }
    }
}
