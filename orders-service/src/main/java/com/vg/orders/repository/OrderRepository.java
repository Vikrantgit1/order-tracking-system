package com.vg.orders.repository;

import com.vg.orders.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o " +
            "WHERE (:id IS NULL OR o.id = :id) " +
            "AND (:customerName IS NULL OR o.customerName = :customerName) " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:item IS NULL OR o.item = :item)")
    List<Orders> findAllWithFilters(@Param("id") Long id,
                               @Param("customerName") String customerName,
                               @Param("status") String status,
                               @Param("item") String item);
}
