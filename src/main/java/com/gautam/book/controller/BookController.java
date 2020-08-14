package com.gautam.book.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.gautam.book.bean.Book;
import com.gautam.book.exception.BookNotFoundException;
import com.gautam.book.repository.BookRepository;
import com.gautam.book.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
	
	@Autowired
	BookService bookService;
	
	@Autowired
	Environment environment;
	
	@Autowired
	BookRepository bookRepository;
	
	@PostMapping
	public ResponseEntity<?> createBook(@Valid @RequestBody Book book, UriComponentsBuilder builder) {
		try {
			bookService.insertBook(book);
			HttpHeaders headers=new HttpHeaders();
			headers.setLocation(builder.path("/api/books/{isbn}").buildAndExpand(book.getIsbn()).toUri());		
			return new ResponseEntity<>(headers, HttpStatus.OK);
		} catch (Exception e) {
			throw e;
//			throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Exists", e);
//			e.printStackTrace();
//			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/{isbn}")
	public ResponseEntity<Book> getBook(@PathVariable("isbn") String isbn) {
		return bookService.findByIsbn(isbn).map(
				book-> new ResponseEntity<>(book, HttpStatus.OK)
				)
				.orElseThrow(()->new BookNotFoundException(isbn));
	}
	
	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks(@PageableDefault(size=50) Pageable pageable,
			@RequestParam(required=false, defaultValue="id") String sort,
			@RequestParam(required=false, defaultValue="asc") String order) {
		final PageRequest pageRequest=PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("asc".equals(order)?Sort.Direction.ASC:Sort.Direction.DESC, sort));
		Page<Book> bookPage=bookRepository.findAll(pageRequest);
		if(bookPage.getContent().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			long totalBooks=bookPage.getTotalElements();
			long nbPageBook= bookPage.getNumberOfElements();
			HttpHeaders headers=new HttpHeaders();
			headers.add("X-Total-Count", String.valueOf(totalBooks));
			if(nbPageBook < totalBooks) {
				headers.add("first", buildPageUri(PageRequest.of(0, bookPage.getSize())));
				headers.add("last", buildPageUri(PageRequest.of(bookPage.getTotalPages()-1,  bookPage.getSize())));
				if(bookPage.hasNext()) {
					headers.add("next", buildPageUri(bookPage.nextPageable()));
				}
				if(bookPage.hasPrevious()) {
					headers.add("prev", buildPageUri(bookPage.previousPageable()));
				}
				return new ResponseEntity<>(bookPage.getContent(), headers, HttpStatus.PARTIAL_CONTENT);
			} else {
				return new ResponseEntity<>(bookPage.getContent(), headers, HttpStatus.OK);
			}
		}
	}
	
	private String buildPageUri(Pageable page) {
		return UriComponentsBuilder.fromUriString("app/v1/books").query("page={page}&size={size}").buildAndExpand(page.getPageNumber(), page.getPageSize()).toString();
	}
	
	@DeleteMapping("/{isbn}")
	public ResponseEntity<?> deleteBook(@PathVariable("isbn") String isbn) {
		return bookService.deleteBook(isbn);
	}
	
	@PutMapping("/{isbn}")
	public ResponseEntity<Book> updateBook(@PathVariable("isbn") String isbn, @Valid @RequestBody Book book) {
		return bookService.updateBook(isbn, book);
	}
	
	@PutMapping("/{isbn}/{description}")
	public ResponseEntity<Book> updateBookDescription(@PathVariable("isbn") String isbn, @PathVariable("description") String description) {
		return bookService.updateBookDescription(isbn, description);
	}
	
}
