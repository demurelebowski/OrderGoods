package com.drg.orderapi.services.impl;

import com.drg.orderapi.dto.ClientDTO;
import com.drg.orderapi.entities.Client;
import com.drg.orderapi.exceptions.ClientNotFoundException;
import com.drg.orderapi.repositories.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientServiceImplTests {

	private ClientServiceImpl service;
	private final ClientRepository repository = Mockito.mock(ClientRepository.class);
	private final Client templateClient = new Client(1L, "Test", "test@gmail.com", "", "", "", "", "", "");

	@BeforeEach
	void setUp() {
		service = new ClientServiceImpl(repository);
		var list = new ArrayList<Client>(List.of(templateClient));
		Mockito.when(repository.findById(1L))
				.thenReturn(java.util.Optional.of(templateClient));
		Mockito.when(repository.findAll())
				.thenReturn(list);
	}

	private void assertClientsAreEqual(ClientDTO actual, ClientDTO expected) {
		assertAll(() -> actual.getEmail()
				.equals(expected.getEmail()), () -> actual.getName()
				.equals(expected.getName()), () -> actual.getId()
				.equals(expected.getId()));
	}

	@Test
	@DisplayName("Should return valid client dto when id is valid")
	void testFindById_shouldReturnValidClientWhenIdIsValid() {
		var actual = service.findById(1L);
		var expected = new ClientDTO(new Client(1L, "Test", "test@gmail.com", "", "", "", "", "", ""));
		assertClientsAreEqual(actual, expected);
	}

	@Test
	@DisplayName("Should throw ClientNotFoundException when given invalid id")
	void testFindById_shouldThrowClientNotFoundExceptionWhenGivenInvalidId() {
		assertThrows(ClientNotFoundException.class, () -> service.findById(10L));
	}

	@Test
	@DisplayName("Should return list of clients")
	void testFindAll_shouldReturnListOfClients() {
		var actual = service.findAll();
		Assertions.assertAll(() -> assertNotNull(actual), () -> assertEquals(actual.get(0)
				.getName(), templateClient.getName()), () -> assertEquals(actual.get(0)
				.getEmail(), templateClient.getEmail()), () -> assertEquals(actual.get(0)
				.getId(), templateClient.getId()));
	}

	@Test
	@DisplayName("Should return empty list when database is empty")
	void testFindAll_ShouldReturnEmptyListWhenDatabaseIsEmpty() {
		Mockito.when(repository.findAll())
				.thenReturn(new ArrayList<Client>());
		var expected = service.findAll();
		assertTrue(expected.isEmpty());
	}

}
