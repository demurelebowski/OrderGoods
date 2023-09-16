package com.drg.orderapi.controllers.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
class ErrorHandlerControllerAdvice {
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
		return new ValidationErrorResponse(e.getConstraintViolations()
				.stream()
				.map(violation -> new Violation(violation.getPropertyPath()
						.toString(), violation.getMessage()))
				.collect(Collectors.toList()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return new ValidationErrorResponse(e.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
				.collect(Collectors.toList()));
	}
}
