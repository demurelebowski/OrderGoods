package com.drg.orderapi.services.impl;

import com.drg.orderapi.dto.ProductDTO;
import com.drg.orderapi.entities.Product;
import com.drg.orderapi.exceptions.ProductNotFoundException;
import com.drg.orderapi.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceImplTest {

	private ProductServiceImpl productService;
	private ProductRepository productRepository;

	@BeforeEach
	public void setUp() {
		productRepository = mock(ProductRepository.class);
		productService = new ProductServiceImpl(productRepository);
	}

	@Test
	public void testFindAll() {
		List<Product> products = Arrays.asList(new Product(1L, "Product 1", 10.0, 2), new Product(2L, "Product 2", 15.0, 3));
		when(productRepository.findAll()).thenReturn(products);

		List<ProductDTO> productDTOs = productService.findAll();

		assertEquals(2, productDTOs.size());
		assertEquals("Product 1", productDTOs.get(0)
				.getName());
		assertEquals("Product 2", productDTOs.get(1)
				.getName());
	}

	@Test
	public void testFindById() {
		Long productId = 1L;
		Product product = new Product(productId, "Product 1", 10.0, 4);
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		ProductDTO productDTO = productService.findById(productId);

		assertNotNull(productDTO);
		assertEquals(productId, productDTO.getId());
		assertEquals("Product 1", productDTO.getName());
	}

	@Test
	public void testFindByIdNotFound() {
		Long productId = 1L;
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> productService.findById(productId));
	}

	@Test
	public void testInsert() {
		ProductDTO productDTO = new ProductDTO();
		Product savedProduct = new Product(1L, "New Product", 20.0, 1);
		when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

		ProductDTO savedProductDTO = productService.insert(productDTO);

		assertNotNull(savedProductDTO);
		assertEquals(1L, savedProductDTO.getId());
		assertEquals("New Product", savedProductDTO.getName());
	}

	@Test
	public void testDelete() {
		Long productId = 1L;
		Product product = new Product(productId, "Product 1", 10.0, 2);
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		productService.delete(productId);

		verify(productRepository, times(1)).deleteById(productId);
	}

	@Test
	public void testDeleteNotFound() {
		Long productId = 1L;
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		assertThrows(ProductNotFoundException.class, () -> productService.delete(productId));
	}
}
