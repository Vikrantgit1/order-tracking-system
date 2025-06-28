package com.vg.orders.orders_service.repository;

import com.vg.orders.orders_service.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
