package com.vg.tracking.kafka;

import com.vg.orders.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventsListener {

    private final RedisTemplate<String, OrderCreatedEvent> redis;

    @KafkaListener(topics = "orders", groupId = "tracking-service")
    public void handleOrder(OrderCreatedEvent event){
        System.out.println("📩 Received event: " + event);
        redis.opsForValue().set("order:" + event.getOrderId(), event);
    }
}
