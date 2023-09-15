package com.drg.orderapi.entities;

import com.drg.orderapi.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Double price;

	@Column(nullable = false)
	private Integer quantity;

	public Product(ProductDTO productDTO) {
		this.name = productDTO.getName();
		this.price = productDTO.getPrice();
		this.quantity = productDTO.getQuantity();
	}

	public Product(Long id) {
		this.id = id;
	}
}
