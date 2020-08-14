package com.gautam.book.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.gautam.book.bean.Book;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Long>{
	
	public Optional<Book> findByIsbn(String isbn);
	
}
