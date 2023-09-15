package com.drg.orderapi.services.impl;

import com.drg.orderapi.dto.OrderDTO;
import com.drg.orderapi.entities.Order;
import com.drg.orderapi.entities.OrderItem;
import com.drg.orderapi.entities.Product;
import com.drg.orderapi.enums.OrderStatus;
import com.drg.orderapi.exceptions.InsufficientProductQuantityException;
import com.drg.orderapi.exceptions.OrderNotFoundException;
import com.drg.orderapi.exceptions.ProductNotFoundException;
import com.drg.orderapi.exceptions.ValidationException;
import com.drg.orderapi.repositories.OrderRepository;
import com.drg.orderapi.repositories.ProductRepository;
import com.drg.orderapi.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository repository;
	private final ProductRepository productRepository;

	public OrderServiceImpl(OrderRepository repository, ProductRepository productRepository) {
		this.repository = repository;
		this.productRepository = productRepository;
	}

	@Override
	public List<OrderDTO> findAll() {
		List<Order> list = repository.findAll();
		return list.stream()
				.map(OrderDTO::new)
				.collect(Collectors.toList());
	}

	@Override
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
		checkProductAvailability(order);
		Order orderSaved = repository.save(order);
		updateProductQuantities(order);

		return new OrderDTO(orderSaved);
	}

	public OrderDTO update(OrderDTO orderDTO) {
		Order existingOrder = FindOrderById(orderDTO.getId());
		existingOrder.setStatus(orderDTO.getStatus());

		Order orderSaved = repository.save(existingOrder);
		return new OrderDTO(orderSaved);
	}

	@Transactional
	public void deleteNotPaidOrdersOlderThanTenMinutes() {
		try {
			List<Order> ordersToDelete = findNotPaidOrdersOlderThanTenMinutes();

			for (Order order : ordersToDelete) {
				restoreProductQuantities(order);
				repository.delete(order);
			}

		} catch (Exception e) {
			System.out.println("An error occurred while attempting to automatically delete unpaid orders.");
			e.printStackTrace();
		}
	}

	private List<Order> findNotPaidOrdersOlderThanTenMinutes() {
		Instant tenMinutesAgo = Instant.now()
				.minus(10, ChronoUnit.MINUTES);

		return repository.findAll()
				.stream()
				.filter(order -> OrderStatus.WAITING.equals(order.getStatus()) && order.getMoment()
						.isBefore(tenMinutesAgo))
				.collect(Collectors.toList());
	}

	private void checkProductAvailability(Order order) {
		for (OrderItem item : order.getItems()) {
			Product product = getProductById(item.getProduct()
					.getId());
			if (product.getQuantity() < item.getQuantity()) {
				throw new InsufficientProductQuantityException(product.getName());
			}
		}
	}

	private void updateProductQuantities(Order order) {
		for (OrderItem item : order.getItems()) {
			Product product = getProductById(item.getProduct()
					.getId());
			setNewQuantityForProduct(product, product.getQuantity() - item.getQuantity());
		}
	}

	private void restoreProductQuantities(Order order) {
		for (OrderItem item : order.getItems()) {
			Product product = item.getProduct();
			setNewQuantityForProduct(product, product.getQuantity() + item.getQuantity());
		}
	}

	private void setNewQuantityForProduct(Product product, Integer quantity) {
		product.setQuantity(quantity);
		productRepository.save(product);
	}

	private Product getProductById(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException(productId));
	}
}
