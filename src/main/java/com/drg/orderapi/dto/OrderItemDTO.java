package com.drg.orderapi.dto;

import com.drg.orderapi.entities.OrderItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class OrderItemDTO {

	private Long id;
	@NotNull(message = "Quantity can't be null or empty")
	@Min(value = 1, message = "Quantity can't be lower than 1")
	private Integer quantity;
	@NotNull(message = "Price can't be null or empty")
	private Double price;
	private String product;
	private Long order;
	public double getSubTotal;

	public OrderItemDTO(@NonNull OrderItem orderItem) {
		id = orderItem.getProduct() == null ?
				null :
				orderItem.getProduct()
						.getId();
		quantity = orderItem.getQuantity();
		price = orderItem.getPrice();
		product = orderItem.getProduct() == null ?
				null :
				orderItem.getProduct()
						.getName();
		order = orderItem.getOrder() == null ?
				null :
				orderItem.getOrder()
						.getId();
		getSubTotal = orderItem.getSubTotal();
	}
}
