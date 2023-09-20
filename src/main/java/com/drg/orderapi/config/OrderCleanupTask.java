package com.drg.orderapi.config;

import com.drg.orderapi.services.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderCleanupTask {
	private final OrderService service;
	@Value("${order.cleanup.fixedDelay}")
	private long fixedDelay;

	public OrderCleanupTask(OrderService service) {
		this.service = service;
	}

	@Scheduled(fixedDelayString = "${order.cleanup.fixedDelay}")
	public void deleteExpiredOrders() {
		service.deleteOldNotPaidOrders();
	}
}
