package com.drg.orderapi.config;

import com.drg.orderapi.services.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderCleanupTask {
	private final OrderService service;

	public OrderCleanupTask(OrderService service) {
		this.service = service;
	}

	@Scheduled(fixedDelay = 600000) // 10 minutes in milliseconds
	public void deleteExpiredOrders() {
		service.deleteNotPaidOrdersOlderThanTenMinutes();
	}
}
