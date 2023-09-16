package com.drg.orderapi.entities;

import com.drg.orderapi.dto.OrderDTO;
import com.drg.orderapi.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "tb_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Instant moment;

	@Column(nullable = false)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private Client client;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> items = new ArrayList<>();

	public double getTotal() {
		return items.stream()
				.mapToDouble(OrderItem::getSubTotal)
				.sum();
	}

	public Order(@NonNull OrderDTO orderDTO) {
		this.id = orderDTO.getId();
		this.status = orderDTO.getStatus() != null ? orderDTO.getStatus() : OrderStatus.WAITING;
		this.client = orderDTO.getClient() != null ? new Client(orderDTO.getClient()) : null;
		this.moment = orderDTO.getMoment() != null ? orderDTO.getMoment() : Instant.now();
		this.items = orderDTO.getItems() == null ?
				null :
				orderDTO.getItems()
						.stream()
						.map(item -> {
							OrderItem orderItem = new OrderItem(item);
							orderItem.setOrder(this);
							return orderItem;
						})
						.collect(Collectors.toList());
	}
}
