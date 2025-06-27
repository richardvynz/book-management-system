package com.richardvinz.Book_Management_App.service;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookService {

    BookResponseDto createBook(BookRequestDto bookRequestDto);

    BookResponseDto getBookById(Long id);

    Page<BookResponseDto> getAllBooks(Pageable pageable);

    BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto);

    void deleteBook(Long id);

    List<BookResponseDto> searchBooksByAuthor(String author);

    List<BookResponseDto> searchBooksByTitle(String title);

    Page<BookResponseDto> searchBooksByKeyword(String keyword, Pageable pageable);

    List<BookResponseDto> getBooksByYear(Integer year);

    List<BookResponseDto> getBooksByPriceRange(Double minPrice, Double maxPrice);

    List<BookResponseDto> getLowStockBooks(Integer threshold);
}