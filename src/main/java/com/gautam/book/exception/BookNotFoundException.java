package com.gautam.book.exception;

public class BookNotFoundException extends RuntimeException {

	public BookNotFoundException(String isbn) {
		super("Could not find the book for ISBN : "+isbn);
	}
	
}
