package com.drg.orderapi.dto;

import com.drg.orderapi.entities.Order;
import com.drg.orderapi.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {

	private Long id;
	private Instant moment;
	private OrderStatus status;
	private ClientDTO client;
	private List<OrderItemDTO> items = new ArrayList<>();
	private Double getTotal;

	public OrderDTO(Order order) {
		id = order.getId();
		moment = order.getMoment();
		status = order.getStatus();
		client = new ClientDTO(order.getClient());
		order.getItems()
				.forEach(item -> this.items.add(new OrderItemDTO(item)));
		getTotal = order.getTotal();
	}
}
