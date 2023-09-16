package com.drg.orderapi.controllers.controllerAdvice;

import lombok.Data;

import java.util.List;

@Data
public class ValidationErrorResponse {
	private List<Violation> violations;

	public ValidationErrorResponse(List<Violation> violations) {
		this.violations = violations;
	}
}
