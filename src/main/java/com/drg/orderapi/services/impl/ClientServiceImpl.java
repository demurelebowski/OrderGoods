package com.drg.orderapi.services.impl;

import com.drg.orderapi.dto.ClientDTO;
import com.drg.orderapi.entities.Client;
import com.drg.orderapi.exceptions.ClientNotFoundException;
import com.drg.orderapi.repositories.ClientRepository;
import com.drg.orderapi.services.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

	public ClientRepository repository;

	public ClientServiceImpl(ClientRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ClientDTO> findAll() {
		List<Client> list = repository.findAll();
		return list.stream()
				.map(ClientDTO::new)
				.collect(Collectors.toList());
	}

	@Override
	public ClientDTO findById(Long id) throws ClientNotFoundException {
		return new ClientDTO(repository.findById(id)
				.orElseThrow(() -> new ClientNotFoundException(id)));
	}

	@Override
	public ClientDTO insert(ClientDTO dto) {
		Client clientToSave = repository.save(new Client(dto));
		return new ClientDTO(clientToSave);
	}

	@Override
	public void delete(Long id) {
		this.findById(id);
		repository.deleteById(id);
	}
}
