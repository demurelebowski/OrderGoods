package com.drg.orderapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InsufficientProductQuantityException extends RuntimeException {
	public InsufficientProductQuantityException(String productName) {
		super("Insufficient product quantity: " + productName);
	}
}
