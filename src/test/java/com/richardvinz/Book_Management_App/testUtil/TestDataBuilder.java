package com.richardvinz.Book_Management_App.testUtil;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import com.richardvinz.Book_Management_App.entity.Book;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


public class TestDataBuilder {

    // Standard valid book data
    public static BookRequestDto createValidBookRequestDto() {
        return BookRequestDto.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("978-0-123456-78-9")
                .price(29.99)
                .stockQuantity(100)
                .publishedYear(2023)
                .description("Test Description")
                .build();
    }

    public static BookResponseDto createValidBookResponseDto() {
        return BookResponseDto.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .isbn("978-0-123456-78-9")
                .price(29.99)
                .stockQuantity(100)
                .publishedYear(2023)
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Book createValidBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("978-0-123456-78-9");
        book.setPrice(29.99);
        book.setStockQuantity(100);
        book.setPublishedYear(2023);
        book.setDescription("Test Description");
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        return book;
    }

    // Invalid book data for validation testing
    public static BookRequestDto createInvalidBookRequestDto() {
        return BookRequestDto.builder()
                .title("") // Invalid - empty
                .author("") // Invalid - empty
                .isbn("invalid-isbn") // Invalid format
                .price(-10.0) // Invalid - negative
                .stockQuantity(-5) // Invalid - negative
                .publishedYear(3000) // Invalid - future year
                .description("")
                .build();
    }

    // Custom book creation methods
    public static BookRequestDto createBookRequestDto(String title, String author, String isbn) {
        return BookRequestDto.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .price(29.99)
                .stockQuantity(100)
                .publishedYear(2023)
                .description("Test Description")
                .build();
    }

    public static BookRequestDto createBookRequestDto(String title, String author, String isbn, Double price, Integer stock) {
        return BookRequestDto.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .price(price)
                .stockQuantity(stock)
                .publishedYear(2023)
                .description("Test Description")
                .build();
    }

    public static BookResponseDto createBookResponseDto(Long id, String title, String author, String isbn) {
        return BookResponseDto.builder()
                .id(id)
                .title(title)
                .author(author)
                .isbn(isbn)
                .price(29.99)
                .stockQuantity(100)
                .publishedYear(2023)
                .description("Test Description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Book createBook(Long id, String title, String author, String isbn) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(29.99);
        book.setStockQuantity(100);
        book.setPublishedYear(2023);
        book.setDescription("Test Description");
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        return book;
    }

    // Edge case data
    public static BookRequestDto createBookWithNullFields() {
        return BookRequestDto.builder()
                .title(null)
                .author(null)
                .isbn(null)
                .price(null)
                .stockQuantity(null)
                .publishedYear(null)
                .description(null)
                .build();
    }

    public static BookRequestDto createBookWithEmptyFields() {
        return BookRequestDto.builder()
                .title("")
                .author("")
                .isbn("")
                .price(0.0)
                .stockQuantity(0)
                .publishedYear(0)
                .description("")
                .build();
    }

    public static BookRequestDto createBookWithMinimumValues() {
        return BookRequestDto.builder()
                .title("A")
                .author("B")
                .isbn("978-0-000000-00-0")
                .price(0.01)
                .stockQuantity(1)
                .publishedYear(1000)
                .description("Min description")
                .build();
    }

    public static BookRequestDto createBookWithMaximumValues() {
        return BookRequestDto.builder()
                .title("This is a very long book title that might test the maximum length constraints")
                .author("This is a very long author name that might test the maximum length constraints")
                .isbn("978-9-999999-99-9")
                .price(9999.99)
                .stockQuantity(999999)
                .publishedYear(2024)
                .description("This is a very long description that might test the maximum length constraints for the description field")
                .build();
    }

    public static BookRequestDto createLowStockBook() {
        return BookRequestDto.builder()
                .title("Low Stock Book")
                .author("Test Author")
                .isbn("978-0-123456-78-9")
                .price(29.99)
                .stockQuantity(5) // Low stock
                .publishedYear(2023)
                .description("Low stock book description")
                .build();
    }

    public static BookRequestDto createExpensiveBook() {
        return BookRequestDto.builder()
                .title("Expensive Book")
                .author("Premium Author")
                .isbn("978-0-123456-78-9")
                .price(999.99) // Expensive
                .stockQuantity(100)
                .publishedYear(2023)
                .description("Premium expensive book description")
                .build();
    }

    // Multiple books for testing lists and pagination
    public static List<Book> createMultipleBooks() {
        return Arrays.asList(
                createBook(1L, "Java Programming", "John Doe", "978-1-111111-11-1"),
                createBook(2L, "Python Guide", "Jane Smith", "978-2-222222-22-2"),
                createBook(3L, "JavaScript Basics", "John Doe", "978-3-333333-33-3"),
                createBook(4L, "Spring Boot Mastery", "Alice Johnson", "978-4-444444-44-4"),
                createBook(5L, "Database Design", "Bob Wilson", "978-5-555555-55-5")
        );
    }

    public static List<BookRequestDto> createMultipleBookRequests() {
        return Arrays.asList(
                createBookRequestDto("Java Programming", "John Doe", "978-1-111111-11-1"),
                createBookRequestDto("Python Guide", "Jane Smith", "978-2-222222-22-2"),
                createBookRequestDto("JavaScript Basics", "John Doe", "978-3-333333-33-3"),
                createBookRequestDto("Spring Boot Mastery", "Alice Johnson", "978-4-444444-44-4"),
                createBookRequestDto("Database Design", "Bob Wilson", "978-5-555555-55-5")
        );
    }

    public static List<BookResponseDto> createMultipleBookResponses() {
        return Arrays.asList(
                createBookResponseDto(1L, "Java Programming", "John Doe", "978-1-111111-11-1"),
                createBookResponseDto(2L, "Python Guide", "Jane Smith", "978-2-222222-22-2"),
                createBookResponseDto(3L, "JavaScript Basics", "John Doe", "978-3-333333-33-3"),
                createBookResponseDto(4L, "Spring Boot Mastery", "Alice Johnson", "978-4-444444-44-4"),
                createBookResponseDto(5L, "Database Design", "Bob Wilson", "978-5-555555-55-5")
        );
    }

    // Books by specific criteria for search testing
    public static List<Book> createBooksByAuthor(String author) {
        return Arrays.asList(
                createBook(1L, "First Book by " + author, author, "978-1-111111-11-1"),
                createBook(2L, "Second Book by " + author, author, "978-2-222222-22-2")
        );
    }

    public static List<Book> createBooksByYear(Integer year) {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book from " + year);
        book1.setAuthor("Author 1");
        book1.setIsbn("978-1-111111-11-1");
        book1.setPrice(29.99);
        book1.setStockQuantity(100);
        book1.setPublishedYear(year);
        book1.setDescription("Book from " + year + " description");
        book1.setCreatedAt(LocalDateTime.now());
        book1.setUpdatedAt(LocalDateTime.now());

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Another Book from " + year);
        book2.setAuthor("Author 2");
        book2.setIsbn("978-2-222222-22-2");
        book2.setPrice(39.99);
        book2.setStockQuantity(50);
        book2.setPublishedYear(year);
        book2.setDescription("Another book from " + year + " description");
        book2.setCreatedAt(LocalDateTime.now());
        book2.setUpdatedAt(LocalDateTime.now());

        return Arrays.asList(book1, book2);
    }

    public static List<Book> createBooksByPriceRange(Double minPrice, Double maxPrice) {
        Double midPrice = (minPrice + maxPrice) / 2;

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Affordable Book");
        book1.setAuthor("Author 1");
        book1.setIsbn("978-1-111111-11-1");
        book1.setPrice(minPrice + 1.0);
        book1.setStockQuantity(100);
        book1.setPublishedYear(2023);
        book1.setDescription("Affordable book description");
        book1.setCreatedAt(LocalDateTime.now());
        book1.setUpdatedAt(LocalDateTime.now());

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Mid-range Book");
        book2.setAuthor("Author 2");
        book2.setIsbn("978-2-222222-22-2");
        book2.setPrice(midPrice);
        book2.setStockQuantity(50);
        book2.setPublishedYear(2023);
        book2.setDescription("Mid-range book description");
        book2.setCreatedAt(LocalDateTime.now());
        book2.setUpdatedAt(LocalDateTime.now());

        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Premium Book");
        book3.setAuthor("Author 3");
        book3.setIsbn("978-3-333333-33-3");
        book3.setPrice(maxPrice - 1.0);
        book3.setStockQuantity(25);
        book3.setPublishedYear(2023);
        book3.setDescription("Premium book description");
        book3.setCreatedAt(LocalDateTime.now());
        book3.setUpdatedAt(LocalDateTime.now());

        return Arrays.asList(book1, book2, book3);
    }

    public static List<Book> createLowStockBooks(Integer threshold) {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Almost Out of Stock");
        book1.setAuthor("Author 1");
        book1.setIsbn("978-1-111111-11-1");
        book1.setPrice(29.99);
        book1.setStockQuantity(threshold - 1);
        book1.setPublishedYear(2023);
        book1.setDescription("Almost out of stock book description");
        book1.setCreatedAt(LocalDateTime.now());
        book1.setUpdatedAt(LocalDateTime.now());

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Very Low Stock");
        book2.setAuthor("Author 2");
        book2.setIsbn("978-2-222222-22-2");
        book2.setPrice(39.99);
        book2.setStockQuantity(1);
        book2.setPublishedYear(2023);
        book2.setDescription("Very low stock book description");
        book2.setCreatedAt(LocalDateTime.now());
        book2.setUpdatedAt(LocalDateTime.now());

        return Arrays.asList(book1, book2);
    }

    // Utility methods for comprehensive testing
    public static Book createBookWithId(Long id) {
        Book book = new Book();
        book.setId(id);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("978-0-123456-78-9");
        book.setPrice(29.99);
        book.setStockQuantity(100);
        book.setPublishedYear(2023);
        book.setDescription("Test Description");
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        return book;
    }

    public static Book createBookWithoutId() {
        Book book = new Book();
        book.setId(null);  // Explicitly set to null
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("978-0-123456-78-9");
        book.setPrice(29.99);
        book.setStockQuantity(100);
        book.setPublishedYear(2023);
        book.setDescription("Test Description");
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        return book;
    }

    // Additional utility methods for comprehensive testing
    public static Book createBookWithCustomData(Long id, String title, String author, String isbn,
                                                Double price, Integer stock, Integer year, String description) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(price);
        book.setStockQuantity(stock);
        book.setPublishedYear(year);
        book.setDescription(description);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());
        return book;
    }

    public static BookResponseDto createBookResponseWithCustomData(Long id, String title, String author,
                                                                   String isbn, Double price, Integer stock, String description) {
        return BookResponseDto.builder()
                .id(id)
                .title(title)
                .author(author)
                .isbn(isbn)
                .price(price)
                .stockQuantity(stock)
                .publishedYear(2023)
                .description(description)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}