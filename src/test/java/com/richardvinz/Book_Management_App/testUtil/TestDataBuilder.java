package com.richardvinz.Book_Management_App.testUtil;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import com.richardvinz.Book_Management_App.entity.Book;

import java.time.LocalDateTime;

public class TestDataBuilder {

    public static BookRequestDto createValidBookRequestDto() {
        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("Test Book");
        dto.setAuthor("Test Author");
        dto.setIsbn("978-0-123456-78-9");
        dto.setPublishedYear(2023);
        dto.setDescription("Test description");
        dto.setPrice(29.99);
        dto.setStockQuantity(100);
        return dto;
    }

    public static BookRequestDto createBookRequestDto(String title, String author, String isbn) {
        BookRequestDto dto = createValidBookRequestDto();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setIsbn(isbn);
        return dto;
    }

    public static BookRequestDto createInvalidBookRequestDto() {
        BookRequestDto dto = new BookRequestDto();
        dto.setTitle(""); // Invalid - empty title
        dto.setAuthor(""); // Invalid - empty author
        dto.setIsbn("invalid-isbn"); // Invalid ISBN format
        dto.setPublishedYear(3000); // Invalid - future year
        dto.setPrice(-10.0); // Invalid - negative price
        dto.setStockQuantity(-5); // Invalid - negative stock
        return dto;
    }

    public static Book createValidBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("978-0-123456-78-9");
        book.setPublishedYear(2023);
        book.setDescription("Test description");
        book.setPrice(29.99);
        book.setStockQuantity(100);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        return book;
    }

    public static Book createBook(Long id, String title, String author, String isbn) {
        Book book = createValidBook();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return book;
    }

    public static BookResponseDto createValidBookResponseDto() {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(1L);
        dto.setTitle("Test Book");
        dto.setAuthor("Test Author");
        dto.setIsbn("978-0-123456-78-9");
        dto.setPublishedYear(2023);
        dto.setDescription("Test description");
        dto.setPrice(29.99);
        dto.setStockQuantity(100);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }
}