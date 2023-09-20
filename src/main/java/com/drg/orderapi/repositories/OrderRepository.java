package com.drg.orderapi.repositories;

import com.drg.orderapi.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("SELECT o FROM Order o WHERE o.status = 0 AND o.moment < :minutesAgo")
	List<Order> findOldNotPaidOrders(Instant minutesAgo);
}
