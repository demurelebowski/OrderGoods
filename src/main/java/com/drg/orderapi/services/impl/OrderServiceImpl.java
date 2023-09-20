package com.drg.orderapi.services.impl;

import com.drg.orderapi.dto.OrderDTO;
import com.drg.orderapi.entities.Client;
import com.drg.orderapi.entities.Order;
import com.drg.orderapi.entities.OrderItem;
import com.drg.orderapi.entities.Product;
import com.drg.orderapi.enums.OrderStatus;
import com.drg.orderapi.exceptions.ClientNotFoundException;
import com.drg.orderapi.exceptions.InsufficientProductQuantityException;
import com.drg.orderapi.exceptions.OrderNotFoundException;
import com.drg.orderapi.exceptions.ProductNotFoundException;
import com.drg.orderapi.exceptions.ValidationException;
import com.drg.orderapi.repositories.ClientRepository;
import com.drg.orderapi.repositories.OrderRepository;
import com.drg.orderapi.repositories.ProductRepository;
import com.drg.orderapi.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	private final OrderRepository repository;
	private final ProductRepository productRepository;
	private final ClientRepository clientRepository;
	@Value("${order.cleanup.notPaidOrdersThresholdMinutes}")
	private int notPaidOrdersThresholdMinutes;

	public OrderServiceImpl(OrderRepository repository, ProductRepository productRepository, ClientRepository clientRepository) {
		this.repository = repository;
		this.productRepository = productRepository;
		this.clientRepository = clientRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDTO> findAll() {
		List<Order> list = repository.findAll();
		return list.stream()
				.map(OrderDTO::new)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public OrderDTO findById(Long id) {
		return new OrderDTO(FindOrderById(id));
	}

	private Order FindOrderById(Long id) {
		if (id == null) {
			throw new ValidationException("Order id can not be null.");
		}
		return repository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));
	}

	@Override
	@Transactional
	public OrderDTO insert(OrderDTO orderDTO) {
		Order order = new Order(orderDTO);
		checkClient(order.getClient());
		checkProductAvailability(order);
		Order orderSaved = repository.save(order);
		updateProductQuantities(order);

		return new OrderDTO(orderSaved);
	}

	private void checkClient(Client client) {
		if (client == null) {
			throw new ValidationException("Client can not be null.");
		}
		clientRepository.findById(client.getId())
				.orElseThrow(() -> new ClientNotFoundException(client.getId()));
	}

	@Transactional
	public OrderDTO update(OrderDTO orderDTO) {
		Order existingOrder = FindOrderById(orderDTO.getId());
		existingOrder.setStatus(orderDTO.getStatus());

		Order orderSaved = repository.save(existingOrder);
		return new OrderDTO(orderSaved);
	}

	@Transactional
	public void deleteOldNotPaidOrders() {
		try {
			List<Order> ordersToDelete = findOldNotPaidOrders();
			if (!ordersToDelete.isEmpty()) {
				restoreProductQuantities(ordersToDelete);
				setOrdersStatus(ordersToDelete, OrderStatus.DELETED);
				repository.saveAll(ordersToDelete);
			}
		} catch (Exception e) {
			log.error("An error occurred while attempting to automatically delete unpaid orders.", e);
		}
	}

	private void setOrdersStatus(List<Order> listOrders, OrderStatus status) {
		listOrders
				.forEach(order -> order.setStatus(status));
	}

	private List<Order> findOldNotPaidOrders() {
		Instant minutesAgo = Instant.now()
				.minus(notPaidOrdersThresholdMinutes, ChronoUnit.MINUTES);

		return repository.findOldNotPaidOrders(minutesAgo);
	}

	private void checkProductAvailability(Order order) {
		if (order.getItems() == null) {
			throw new ValidationException("Items can not be null");
		}
		for (OrderItem item : order.getItems()) {
			Product product = getProductById(item.getProduct()
					.getId());
			if (product.getQuantity() < item.getQuantity()) {
				throw new InsufficientProductQuantityException(product.getName());
			}
		}
	}

	private void updateProductQuantities(Order order) {
		List<Product> productList = order.getItems()
				.stream()
				.map(orderItem -> {
					Product product = getProductById(orderItem.getProduct()
							.getId());
					product.setQuantity(product.getQuantity() - orderItem.getQuantity());
					return product;
				})
				.collect(Collectors.toList());

		productRepository.saveAll(productList);
	}

	private void restoreProductQuantities(List<Order> orders) {
		List<Product> productList = orders.stream()
				.flatMap(order -> order.getItems()
						.stream())
				.map(orderItem -> {
					Product product = orderItem.getProduct();
					product.setQuantity(product.getQuantity() + orderItem.getQuantity());
					return product;
				})
				.collect(Collectors.toList());

		productRepository.saveAll(productList);
	}

	private Product getProductById(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException(productId));
	}
}
