package com.gautam.book.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.gautam.book.bean.Book;
import com.gautam.book.exception.BookIsbnAlreadyExists;

public interface BookService {
	
	public Book insertBook(Book book) throws BookIsbnAlreadyExists;
	public Optional<Book> findByIsbn(String isbn);
	public Iterable<Book> getAllBooks();
	public ResponseEntity<Book> updateBook(String isbn, Book book);
	public ResponseEntity<Book> updateBookDescription(String isbn, String description);
	public ResponseEntity<Object> deleteBook(String isbn);
	
}
