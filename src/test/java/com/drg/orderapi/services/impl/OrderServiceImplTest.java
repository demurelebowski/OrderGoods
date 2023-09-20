package com.drg.orderapi.services.impl;

import com.drg.orderapi.dto.OrderDTO;
import com.drg.orderapi.entities.Client;
import com.drg.orderapi.entities.Order;
import com.drg.orderapi.enums.OrderStatus;
import com.drg.orderapi.exceptions.ClientNotFoundException;
import com.drg.orderapi.exceptions.OrderNotFoundException;
import com.drg.orderapi.exceptions.ValidationException;
import com.drg.orderapi.repositories.ClientRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

	private OrderServiceImpl orderService;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ProductRepository productRepository;
	@Mock
	private ClientRepository clientRepository;
	private final Client templateClient = new Client(1L, "Test", "test@gmail.com", "", "", "", "", "", "");

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		orderService = new OrderServiceImpl(orderRepository, productRepository, clientRepository);
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
	public void testInsert_ClientIdNull() {
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING, new Client(), new ArrayList<>());
		OrderDTO orderDTO = new OrderDTO(order);
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		assertThrows(ClientNotFoundException.class, () -> orderService.insert(orderDTO));
	}

	@Test
	public void testInsert_ItemsIsNull() {
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING, templateClient, null);
		OrderDTO orderDTO = new OrderDTO(order);
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(clientRepository.findById(1L)).thenReturn(java.util.Optional.of(templateClient));

		assertThrows(ValidationException.class, () -> orderService.insert(orderDTO));
	}

	@Test
	public void testInsert() {
		Order order = new Order(1L, Instant.now(), OrderStatus.WAITING, templateClient, new ArrayList<>());
		OrderDTO orderDTO = new OrderDTO(order);
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(clientRepository.findById(1L)).thenReturn(java.util.Optional.of(templateClient));
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
	public void testUpdate_OrderIdIsNull() {
		Order order = new Order(null, Instant.now(), OrderStatus.WAITING, new Client(), new ArrayList<>());
		OrderDTO orderDTO = new OrderDTO(order);

		assertThrows(ValidationException.class, () -> orderService.update(orderDTO));
	}

	@Test
	public void testDeleteNotPaidOrdersOlderThanTenMinutesException() {
		Instant tenMinutesAgo = Instant.now()
				.minus(10, ChronoUnit.MINUTES);
		when(orderRepository.findOldNotPaidOrders(tenMinutesAgo)).thenThrow(new RuntimeException());

		assertDoesNotThrow(() -> orderService.deleteOldNotPaidOrders());
	}

	@Test
	public void testDeleteNotPaidOrdersOlderThanTenMinutes() {
		Instant tenMinutesAgo = Instant.now()
				.minus(10, ChronoUnit.MINUTES);
		Order unpaidOrder1 = new Order(1L, tenMinutesAgo.minus(5, ChronoUnit.MINUTES), OrderStatus.WAITING, null, new ArrayList<>());
		Order unpaidOrder2 = new Order(2L, tenMinutesAgo.minus(15, ChronoUnit.MINUTES), OrderStatus.WAITING, null, new ArrayList<>());
		List<Order> unpaidOrders = new ArrayList<>();
		unpaidOrders.add(unpaidOrder1);
		unpaidOrders.add(unpaidOrder2);

		when(orderRepository.findOldNotPaidOrders(any(Instant.class))).thenReturn(unpaidOrders);

		orderService.deleteOldNotPaidOrders();

		verify(orderRepository, times(1)).deleteAll(unpaidOrders);
	}

	@Test
	public void testDeleteNotPaidOrdersOlderThanTenMinutes_NoOrdersToDelete() {
		List<Order> orders = new ArrayList<>();
		when(orderRepository.findOldNotPaidOrders(any(Instant.class))).thenReturn(orders);

		orderService.deleteOldNotPaidOrders();

		verify(orderRepository, never()).deleteAll(orders);
	}

}
