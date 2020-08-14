package com.gautam.book.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gautam.book.bean.ApiError;
import com.gautam.book.exception.BookIsbnAlreadyExists;
import com.gautam.book.exception.BookNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class BookControllerAdvice {
	
	@ExceptionHandler(BookIsbnAlreadyExists.class)
	public ResponseEntity<Object> bookIsbnAlreayExistsExceptionHandler(BookIsbnAlreadyExists ex) {
		ApiError apiError=new ApiError(ex.getMessage(), HttpStatus.CONFLICT.value());
		return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<Object> bookNotFoundExceptionHandler(BookNotFoundException ex) {
		ApiError apiError=new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}
	
}
