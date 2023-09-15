package com.drg.orderapi.dto;

import com.drg.orderapi.entities.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemDTO {

	private Long id;
	private Integer quantity;
	private Double price;
	private String product;
	private Long order;
	public double getSubTotal;

	public OrderItemDTO(OrderItem orderItem) {
		id = orderItem.getProduct()
				.getId();
		quantity = orderItem.getQuantity();
		price = orderItem.getPrice();
		product = orderItem.getProduct()
				.getName();
		order = orderItem.getOrder()
				.getId();
		getSubTotal = orderItem.getSubTotal();
	}
}
