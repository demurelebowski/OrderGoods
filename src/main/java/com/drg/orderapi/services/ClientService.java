package com.drg.orderapi.services;

import com.drg.orderapi.dto.ClientDTO;

import java.util.List;

public interface ClientService {

	List<ClientDTO> findAll();

	ClientDTO findById(Long id);

	ClientDTO insert(ClientDTO dto);

	void delete(Long id);
}
