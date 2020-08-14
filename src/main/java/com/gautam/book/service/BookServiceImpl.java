package com.gautam.book.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gautam.book.bean.Book;
import com.gautam.book.exception.BookIsbnAlreadyExists;
import com.gautam.book.exception.BookNotFoundException;
import com.gautam.book.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	BookRepository bookRepository;
	
	@Override
	public Book insertBook(Book book) throws BookIsbnAlreadyExists {
		if(bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
			throw new BookIsbnAlreadyExists(book.getIsbn());
		} else {
			return bookRepository.save(book);
		}
	}

	@Override
	public Optional<Book> findByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	@Override
	public Iterable<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@Override
	public ResponseEntity<Book> updateBook(String isbn, Book book) {
		return bookRepository.findByIsbn(isbn).map(bookToUpdate-> {
			bookToUpdate.setIsbn(isbn);
			bookToUpdate.setTitle(book.getTitle());
			bookToUpdate.setPublisher(book.getPublisher());
			bookToUpdate.setDescription(book.getDescription());
			bookToUpdate.setAuthors(book.getAuthors());
			bookRepository.save(bookToUpdate);
			return new ResponseEntity<>(bookToUpdate, HttpStatus.OK);
		}).orElseThrow(()-> new BookNotFoundException(isbn));
	}

	@Override
	public ResponseEntity<Book> updateBookDescription(String isbn, String description) {
		return bookRepository.findByIsbn(isbn).map(bookToUpdate-> {
			bookToUpdate.setIsbn(isbn);
			bookToUpdate.setDescription(description);
			bookRepository.save(bookToUpdate);
			return new ResponseEntity<>(bookToUpdate, HttpStatus.OK);
		}).orElseThrow(()-> new BookNotFoundException(isbn));
	}

	@Override
	public ResponseEntity<Object> deleteBook(String isbn) {
		return bookRepository.findByIsbn(isbn).map(book-> {
			bookRepository.delete(book);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}).orElseThrow(()-> new BookNotFoundException(isbn));
	}

}
