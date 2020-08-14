package com.gautam.book.exception;

public class BookIsbnAlreadyExists extends RuntimeException {

	public BookIsbnAlreadyExists(String isbn) {
		super("Book already exists with ISBN : "+isbn);
	}
	
}
