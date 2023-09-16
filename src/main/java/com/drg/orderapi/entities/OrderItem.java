package com.drg.orderapi.entities;

import com.drg.orderapi.dto.OrderItemDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer quantity;

	@Column(nullable = false)
	private Double price;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	public OrderItem(@NonNull OrderItemDTO item) {
		this.price = item.getPrice();
		this.quantity = item.getQuantity();
		this.product = item.getId() == null ? null : new Product(item.getId());
		this.order = new Order();
		this.price = item.getPrice();
	}

	public double getSubTotal() {
		return price * quantity;
	}
}
