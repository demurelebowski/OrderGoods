package com.drg.orderapi.dto;

import com.drg.orderapi.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ProductDTO {

	private Long id;
	@NotNull(message = "Name can't be null or empty")
	@Size(min = 3, max = 25)
	private String name;
	@NotNull(message = "Price can't be null or empty")
	@Min(value = 1, message = "Price can't be lower than 1")
	private Double price;
	@NotNull(message = "Quantity can't be null or empty")
	private Integer quantity;

	public ProductDTO(@NonNull Product product) {
		id = product.getId();
		name = product.getName();
		price = product.getPrice();
		quantity = product.getQuantity();
	}
}
