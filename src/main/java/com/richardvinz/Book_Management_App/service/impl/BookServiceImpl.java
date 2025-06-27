package com.richardvinz.Book_Management_App.service.impl;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import com.richardvinz.Book_Management_App.entity.Book;
import com.richardvinz.Book_Management_App.exception.BookNotFoundException;
import com.richardvinz.Book_Management_App.exception.ValidationException;
import com.richardvinz.Book_Management_App.repository.BookRepository;
import com.richardvinz.Book_Management_App.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookResponseDto createBook(BookRequestDto bookRequestDto) {
        // Check if ISBN already exists
        if (bookRepository.existsByIsbn(bookRequestDto.getIsbn())) {
            throw new ValidationException("Book with ISBN " + bookRequestDto.getIsbn() + " already exists");
        }

        Book book = convertToEntity(bookRequestDto);
        Book savedBook = bookRepository.save(book);
        return convertToResponseDto(savedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
        return convertToResponseDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(this::convertToResponseDto);
    }

    @Override
    public BookResponseDto updateBook(Long id, BookRequestDto bookRequestDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));

        // Check if ISBN is being changed and if new ISBN already exists
        if (!existingBook.getIsbn().equals(bookRequestDto.getIsbn()) &&
                bookRepository.existsByIsbn(bookRequestDto.getIsbn())) {
            throw new ValidationException("Book with ISBN " + bookRequestDto.getIsbn() + " already exists");
        }

        updateBookFromDto(existingBook, bookRequestDto);
        Book updatedBook = bookRepository.save(existingBook);
        return convertToResponseDto(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> searchBooksByAuthor(String author) {
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponseDto> searchBooksByKeyword(String keyword, Pageable pageable) {
        Page<Book> books = bookRepository.findByKeyword(keyword, pageable);
        return books.map(this::convertToResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooksByYear(Integer year) {
        List<Book> books = bookRepository.findByPublishedYear(year);
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooksByPriceRange(Double minPrice, Double maxPrice) {
        List<Book> books = bookRepository.findByPriceBetween(minPrice, maxPrice);
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> getLowStockBooks(Integer threshold) {
        List<Book> books = bookRepository.findByStockQuantityLessThan(threshold);
        return books.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    // Helper methods for conversion
    private Book convertToEntity(BookRequestDto dto) {
        return new Book(
                dto.getTitle(),
                dto.getAuthor(),
                dto.getIsbn(),
                dto.getPublishedYear(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getStockQuantity()
        );
    }

    private BookResponseDto convertToResponseDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setPublishedYear(book.getPublishedYear());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        dto.setStockQuantity(book.getStockQuantity());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        return dto;
    }

    private void updateBookFromDto(Book book, BookRequestDto dto) {
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setPublishedYear(dto.getPublishedYear());
        book.setDescription(dto.getDescription());
        book.setPrice(dto.getPrice());
        book.setStockQuantity(dto.getStockQuantity());
    }
}