package com.drg.orderapi.services.impl;

import com.drg.orderapi.dto.OrderDTO;
import com.drg.orderapi.entities.Client;
import com.drg.orderapi.entities.Order;
import com.drg.orderapi.enums.OrderStatus;
import com.drg.orderapi.exceptions.OrderNotFoundException;
import com.drg.orderapi.repositories.OrderRepository;
import com.drg.orderapi.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

	private OrderServiceImpl orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		orderService = new OrderServiceImpl(orderRepository, productRepository);
	}

	@Test
	public void testFindAll() {
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(1L, Instant.now(), OrderStatus.WAITING, new Client(), new ArrayList<>()));
		when(orderRepository.findAll()).thenReturn(orders);

		List<OrderDTO> orderDTOs = orderService.findAll();

		assertEquals(1, orderDTOs.size());
	}

	@Test
	public void testFindById() {
		Long orderId = 1L;
		Order order = new Order(orderId, Instant.now(), OrderStatus.WAITING, new Client(), new ArrayList<>());
		when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

		OrderDTO orderDTO = orderService.findById(orderId);

		assertNotNull(orderDTO);
		assertEquals(orderId, orderDTO.getId());
	}

	@Test
	public void testFindByIdNotFound() {
		Long orderId = 1L;
		when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.empty());

		assertThrows(OrderNotFoundException.class, () -> orderService.findById(orderId));
	}

	@Test
	public void testInsert() {
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING, new Client(), new ArrayList<>());
		OrderDTO orderDTO = new OrderDTO(order);
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		OrderDTO savedOrderDTO = orderService.insert(orderDTO);

		assertNotNull(savedOrderDTO);
	}

	@Test
	public void testUpdate() {
		Long orderId = 1L;

		Order order = new Order(orderId, Instant.now(), OrderStatus.WAITING, new Client(), new ArrayList<>());
		OrderDTO orderDTO = new OrderDTO(order);
		when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		OrderDTO updatedOrderDTO = orderService.update(orderDTO);

		assertNotNull(updatedOrderDTO);
	}

	@Test
	public void testDeleteNotPaidOrdersOlderThanTenMinutesException() {
		Instant tenMinutesAgo = Instant.now()
				.minus(10, ChronoUnit.MINUTES);
		when(orderRepository.findNotPaidOrdersOlderThanTenMinutes(tenMinutesAgo)).thenThrow(new RuntimeException());

		assertDoesNotThrow(() -> orderService.deleteNotPaidOrdersOlderThanTenMinutes());
	}
}
