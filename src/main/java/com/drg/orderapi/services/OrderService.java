package com.drg.orderapi.services;

import com.drg.orderapi.dto.OrderDTO;

import java.util.List;

public interface OrderService {

	List<OrderDTO> findAll();

	OrderDTO findById(Long id);

	OrderDTO insert(OrderDTO order);

	OrderDTO update(OrderDTO order);

	void deleteExpiredNotPaidOrders();
}
