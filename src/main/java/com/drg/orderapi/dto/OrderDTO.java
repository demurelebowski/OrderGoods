package com.drg.orderapi.dto;

import com.drg.orderapi.entities.Order;
import com.drg.orderapi.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderDTO {

	private Long id;
	private Instant moment;
	private OrderStatus status;
	private ClientDTO client;
	private List<OrderItemDTO> items;
	private Double getTotal;

	public OrderDTO(@NonNull Order order) {
		id = order.getId();
		moment = order.getMoment();
		status = order.getStatus();
		client = order.getClient() == null ? null : new ClientDTO(order.getClient());
		items = order.getItems() == null ?
				null :
				order.getItems()
						.stream()
						.map(OrderItemDTO::new)
						.collect(Collectors.toList());
		getTotal = order.getTotal();
	}
}
