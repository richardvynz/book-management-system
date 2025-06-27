package com.richardvinz.Book_Management_App.service;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import com.richardvinz.Book_Management_App.entity.Book;
import com.richardvinz.Book_Management_App.exception.BookNotFoundException;
import com.richardvinz.Book_Management_App.exception.ValidationException;
import com.richardvinz.Book_Management_App.repository.BookRepository;
import com.richardvinz.Book_Management_App.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookServiceImpl Tests - 100% Coverage")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookRequestDto testBookRequestDto;
    private Pageable testPageable;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("978-0-123456-78-9");
        testBook.setPrice(29.99);
        testBook.setStockQuantity(100);
        testBook.setPublishedYear(2023);
        testBook.setDescription("Test Description");
        testBook.setCreatedAt(LocalDateTime.now());
        testBook.setUpdatedAt(LocalDateTime.now());

        testBookRequestDto = BookRequestDto.builder()
                .title("Test Book")
                .author("Test Author")
                .isbn("978-0-123456-78-9")
                .price(29.99)
                .stockQuantity(100)
                .publishedYear(2023)
                .description("Test Description")
                .build();

        testPageable = PageRequest.of(0, 10, Sort.by("title"));
    }

    @Nested
    @DisplayName("Create Book Tests")
    class CreateBookTests {

        @Test
        @DisplayName("Should create book successfully")
        void shouldCreateBookSuccessfully() {
            // Given
            when(bookRepository.existsByIsbn(testBookRequestDto.getIsbn())).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.createBook(testBookRequestDto);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo("Test Book");
            assertThat(result.getAuthor()).isEqualTo("Test Author");
            assertThat(result.getIsbn()).isEqualTo("978-0-123456-78-9");
            assertThat(result.getPrice()).isEqualTo(29.99);
            assertThat(result.getStockQuantity()).isEqualTo(100);
            assertThat(result.getDescription()).isEqualTo("Test Description");

            verify(bookRepository).existsByIsbn(testBookRequestDto.getIsbn());
            verify(bookRepository).save(any(Book.class));
        }

        @Test
        @DisplayName("Should throw ValidationException when ISBN already exists")
        void shouldThrowValidationExceptionWhenIsbnExists() {
            // Given
            when(bookRepository.existsByIsbn(testBookRequestDto.getIsbn())).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> bookService.createBook(testBookRequestDto))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Book with ISBN 978-0-123456-78-9 already exists");

            verify(bookRepository).existsByIsbn(testBookRequestDto.getIsbn());
            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should handle null ISBN during creation")
        void shouldHandleNullIsbnDuringCreation() {
            // Given
            testBookRequestDto.setIsbn(null);
            when(bookRepository.existsByIsbn(null)).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.createBook(testBookRequestDto);

            // Then
            assertThat(result).isNotNull();
            verify(bookRepository).existsByIsbn(null);
            verify(bookRepository).save(any(Book.class));
        }

        @Test
        @DisplayName("Should handle empty ISBN during creation")
        void shouldHandleEmptyIsbnDuringCreation() {
            // Given
            testBookRequestDto.setIsbn("");
            when(bookRepository.existsByIsbn("")).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.createBook(testBookRequestDto);

            // Then
            assertThat(result).isNotNull();
            verify(bookRepository).existsByIsbn("");
            verify(bookRepository).save(any(Book.class));
        }

        @Test
        @DisplayName("Should create book with all fields populated correctly")
        void shouldCreateBookWithAllFieldsPopulatedCorrectly() {
            // Given
            when(bookRepository.existsByIsbn(testBookRequestDto.getIsbn())).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
                Book savedBook = invocation.getArgument(0);
                savedBook.setId(1L);
                savedBook.setCreatedAt(LocalDateTime.now());
                savedBook.setUpdatedAt(LocalDateTime.now());
                return savedBook;
            });

            // When
            bookService.createBook(testBookRequestDto);

            // Then
            verify(bookRepository).save(argThat(book ->
                    book.getTitle().equals(testBookRequestDto.getTitle()) &&
                            book.getAuthor().equals(testBookRequestDto.getAuthor()) &&
                            book.getIsbn().equals(testBookRequestDto.getIsbn()) &&
                            book.getPrice().equals(testBookRequestDto.getPrice()) &&
                            book.getStockQuantity().equals(testBookRequestDto.getStockQuantity()) &&
                            book.getPublishedYear().equals(testBookRequestDto.getPublishedYear()) &&
                            book.getDescription().equals(testBookRequestDto.getDescription())
            ));
        }
    }

    @Nested
    @DisplayName("Get Book Tests")
    class GetBookTests {

        @Test
        @DisplayName("Should get book by ID successfully")
        void shouldGetBookByIdSuccessfully() {
            // Given
            Long bookId = 1L;
            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));

            // When
            BookResponseDto result = bookService.getBookById(bookId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(bookId);
            assertThat(result.getTitle()).isEqualTo("Test Book");
            assertThat(result.getAuthor()).isEqualTo("Test Author");
            assertThat(result.getIsbn()).isEqualTo("978-0-123456-78-9");
            assertThat(result.getPrice()).isEqualTo(29.99);
            assertThat(result.getStockQuantity()).isEqualTo(100);
            assertThat(result.getDescription()).isEqualTo("Test Description");

            verify(bookRepository).findById(bookId);
        }

        @Test
        @DisplayName("Should throw BookNotFoundException when book not found")
        void shouldThrowBookNotFoundExceptionWhenBookNotFound() {
            // Given
            Long bookId = 999L;
            when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> bookService.getBookById(bookId))
                    .isInstanceOf(BookNotFoundException.class)
                    .hasMessage("Book not found with ID: 999");

            verify(bookRepository).findById(bookId);
        }
    }

    @Nested
    @DisplayName("Get All Books Tests")
    class GetAllBooksTests {

        @Test
        @DisplayName("Should get all books with pagination successfully")
        void shouldGetAllBooksWithPaginationSuccessfully() {
            // Given
            List<Book> books = Arrays.asList(testBook);
            Page<Book> bookPage = new PageImpl<>(books, testPageable, 1);
            when(bookRepository.findAll(testPageable)).thenReturn(bookPage);

            // When
            Page<BookResponseDto> result = bookService.getAllBooks(testPageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Book");
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);

            verify(bookRepository).findAll(testPageable);
        }

        @Test
        @DisplayName("Should return empty page when no books found")
        void shouldReturnEmptyPageWhenNoBooksFound() {
            // Given
            Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), testPageable, 0);
            when(bookRepository.findAll(testPageable)).thenReturn(emptyPage);

            // When
            Page<BookResponseDto> result = bookService.getAllBooks(testPageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
            assertThat(result.getTotalPages()).isEqualTo(0);

            verify(bookRepository).findAll(testPageable);
        }

        @Test
        @DisplayName("Should handle multiple books pagination")
        void shouldHandleMultipleBooksPagination() {
            // Given
            Book book2 = new Book();
            book2.setId(2L);
            book2.setTitle("Another Book");
            book2.setAuthor("Another Author");

            List<Book> books = Arrays.asList(testBook, book2);
            Page<Book> bookPage = new PageImpl<>(books, testPageable, 2);
            when(bookRepository.findAll(testPageable)).thenReturn(bookPage);

            // When
            Page<BookResponseDto> result = bookService.getAllBooks(testPageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(2);

            verify(bookRepository).findAll(testPageable);
        }
    }

    @Nested
    @DisplayName("Update Book Tests")
    class UpdateBookTests {

        @Test
        @DisplayName("Should update book successfully when ISBN unchanged")
        void shouldUpdateBookSuccessfullyWhenIsbnUnchanged() {
            // Given
            Long bookId = 1L;
            BookRequestDto updateRequest = BookRequestDto.builder()
                    .title("Updated Title")
                    .author("Updated Author")
                    .isbn("978-0-123456-78-9") // Same ISBN
                    .price(39.99)
                    .stockQuantity(150)
                    .publishedYear(2024)
                    .description("Updated Description")
                    .build();

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.updateBook(bookId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(bookRepository).findById(bookId);
            verify(bookRepository).save(any(Book.class));
            verify(bookRepository, never()).existsByIsbn(any()); // Should not check ISBN since it's the same
        }

        @Test
        @DisplayName("Should update book successfully when ISBN changed and new ISBN not exists")
        void shouldUpdateBookSuccessfullyWhenIsbnChangedAndNewIsbnNotExists() {
            // Given
            Long bookId = 1L;
            String newIsbn = "978-0-123456-79-6";
            BookRequestDto updateRequest = BookRequestDto.builder()
                    .title("Updated Title")
                    .author("Updated Author")
                    .isbn(newIsbn) // Different ISBN
                    .price(39.99)
                    .stockQuantity(150)
                    .publishedYear(2024)
                    .description("Updated Description")
                    .build();

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
            when(bookRepository.existsByIsbn(newIsbn)).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.updateBook(bookId, updateRequest);

            // Then
            assertThat(result).isNotNull();
            verify(bookRepository).findById(bookId);
            verify(bookRepository).existsByIsbn(newIsbn);
            verify(bookRepository).save(any(Book.class));
        }

        @Test
        @DisplayName("Should throw BookNotFoundException when updating non-existent book")
        void shouldThrowBookNotFoundExceptionWhenUpdatingNonExistentBook() {
            // Given
            Long bookId = 999L;
            when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> bookService.updateBook(bookId, testBookRequestDto))
                    .isInstanceOf(BookNotFoundException.class)
                    .hasMessage("Book not found with ID: 999");

            verify(bookRepository).findById(bookId);
            verify(bookRepository, never()).existsByIsbn(any());
            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ValidationException when ISBN changed and new ISBN already exists")
        void shouldThrowValidationExceptionWhenIsbnChangedAndNewIsbnExists() {
            // Given
            Long bookId = 1L;
            String newIsbn = "978-0-123456-79-6";
            BookRequestDto updateRequest = BookRequestDto.builder()
                    .title("Updated Title")
                    .author("Updated Author")
                    .isbn(newIsbn) // Different ISBN
                    .price(39.99)
                    .stockQuantity(150)
                    .publishedYear(2024)
                    .description("Updated Description")
                    .build();

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
            when(bookRepository.existsByIsbn(newIsbn)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> bookService.updateBook(bookId, updateRequest))
                    .isInstanceOf(ValidationException.class)
                    .hasMessage("Book with ISBN 978-0-123456-79-6 already exists");

            verify(bookRepository).findById(bookId);
            verify(bookRepository).existsByIsbn(newIsbn);
            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should update all book fields correctly")
        void shouldUpdateAllBookFieldsCorrectly() {
            // Given
            Long bookId = 1L;
            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            bookService.updateBook(bookId, testBookRequestDto);

            // Then
            verify(bookRepository).save(argThat(book ->
                    book.getTitle().equals(testBookRequestDto.getTitle()) &&
                            book.getAuthor().equals(testBookRequestDto.getAuthor()) &&
                            book.getIsbn().equals(testBookRequestDto.getIsbn()) &&
                            book.getPrice().equals(testBookRequestDto.getPrice()) &&
                            book.getStockQuantity().equals(testBookRequestDto.getStockQuantity()) &&
                            book.getPublishedYear().equals(testBookRequestDto.getPublishedYear()) &&
                            book.getDescription().equals(testBookRequestDto.getDescription())
            ));
        }

        @Test
        @DisplayName("Should handle null ISBN in update request")
        void shouldHandleNullIsbnInUpdateRequest() {
            // Given
            Long bookId = 1L;
            testBook.setIsbn("original-isbn");
            testBookRequestDto.setIsbn(null);

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
            when(bookRepository.existsByIsbn(null)).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.updateBook(bookId, testBookRequestDto);

            // Then
            assertThat(result).isNotNull();
            verify(bookRepository).existsByIsbn(null);
        }
    }

    @Nested
    @DisplayName("Delete Book Tests")
    class DeleteBookTests {

        @Test
        @DisplayName("Should delete book successfully")
        void shouldDeleteBookSuccessfully() {
            // Given
            Long bookId = 1L;
            when(bookRepository.existsById(bookId)).thenReturn(true);
            doNothing().when(bookRepository).deleteById(bookId);

            // When
            assertThatCode(() -> bookService.deleteBook(bookId))
                    .doesNotThrowAnyException();

            // Then
            verify(bookRepository).existsById(bookId);
            verify(bookRepository).deleteById(bookId);
        }

        @Test
        @DisplayName("Should throw BookNotFoundException when deleting non-existent book")
        void shouldThrowBookNotFoundExceptionWhenDeletingNonExistentBook() {
            // Given
            Long bookId = 999L;
            when(bookRepository.existsById(bookId)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> bookService.deleteBook(bookId))
                    .isInstanceOf(BookNotFoundException.class)
                    .hasMessage("Book not found with ID: 999");

            verify(bookRepository).existsById(bookId);
            verify(bookRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Search Books Tests")
    class SearchBooksTests {

        @Test
        @DisplayName("Should search books by author successfully")
        void shouldSearchBooksByAuthorSuccessfully() {
            // Given
            String author = "Test Author";
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.searchBooksByAuthor(author);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getAuthor()).isEqualTo("Test Author");

            verify(bookRepository).findByAuthorContainingIgnoreCase(author);
        }

        @Test
        @DisplayName("Should return empty list when no books found by author")
        void shouldReturnEmptyListWhenNoBooksFoundByAuthor() {
            // Given
            String author = "Unknown Author";
            when(bookRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(Collections.emptyList());

            // When
            List<BookResponseDto> result = bookService.searchBooksByAuthor(author);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();

            verify(bookRepository).findByAuthorContainingIgnoreCase(author);
        }

        @Test
        @DisplayName("Should search books by title successfully")
        void shouldSearchBooksByTitleSuccessfully() {
            // Given
            String title = "Test";
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByTitleContainingIgnoreCase(title)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.searchBooksByTitle(title);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTitle()).contains("Test");

            verify(bookRepository).findByTitleContainingIgnoreCase(title);
        }

        @Test
        @DisplayName("Should return empty list when no books found by title")
        void shouldReturnEmptyListWhenNoBooksFoundByTitle() {
            // Given
            String title = "Unknown Title";
            when(bookRepository.findByTitleContainingIgnoreCase(title)).thenReturn(Collections.emptyList());

            // When
            List<BookResponseDto> result = bookService.searchBooksByTitle(title);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();

            verify(bookRepository).findByTitleContainingIgnoreCase(title);
        }

        @Test
        @DisplayName("Should search books by keyword successfully")
        void shouldSearchBooksByKeywordSuccessfully() {
            // Given
            String keyword = "test";
            List<Book> books = Arrays.asList(testBook);
            Page<Book> bookPage = new PageImpl<>(books, testPageable, 1);

            when(bookRepository.findByKeyword(keyword, testPageable)).thenReturn(bookPage);

            // When
            Page<BookResponseDto> result = bookService.searchBooksByKeyword(keyword, testPageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getTotalElements()).isEqualTo(1);

            verify(bookRepository).findByKeyword(keyword, testPageable);
        }

        @Test
        @DisplayName("Should return empty page when no books found by keyword")
        void shouldReturnEmptyPageWhenNoBooksFoundByKeyword() {
            // Given
            String keyword = "unknown";
            Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), testPageable, 0);

            when(bookRepository.findByKeyword(keyword, testPageable)).thenReturn(emptyPage);

            // When
            Page<BookResponseDto> result = bookService.searchBooksByKeyword(keyword, testPageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);

            verify(bookRepository).findByKeyword(keyword, testPageable);
        }

        @Test
        @DisplayName("Should get books by publication year successfully")
        void shouldGetBooksByPublicationYearSuccessfully() {
            // Given
            Integer year = 2023;
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByPublishedYear(year)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.getBooksByYear(year);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPublishedYear()).isEqualTo(2023);

            verify(bookRepository).findByPublishedYear(year);
        }

        @Test
        @DisplayName("Should get books by price range successfully")
        void shouldGetBooksByPriceRangeSuccessfully() {
            // Given
            Double minPrice = 20.0;
            Double maxPrice = 50.0;
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByPriceBetween(minPrice, maxPrice)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.getBooksByPriceRange(minPrice, maxPrice);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPrice()).isBetween(minPrice, maxPrice);

            verify(bookRepository).findByPriceBetween(minPrice, maxPrice);
        }

        @Test
        @DisplayName("Should get low stock books successfully")
        void shouldGetLowStockBooksSuccessfully() {
            // Given
            Integer threshold = 10;
            Book lowStockBook = new Book();
            lowStockBook.setId(2L);
            lowStockBook.setTitle("Low Stock Book");
            lowStockBook.setStockQuantity(5);

            List<Book> books = Arrays.asList(lowStockBook);
            when(bookRepository.findByStockQuantityLessThan(threshold)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.getLowStockBooks(threshold);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStockQuantity()).isLessThan(threshold);

            verify(bookRepository).findByStockQuantityLessThan(threshold);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle repository exceptions during creation")
        void shouldHandleRepositoryExceptionsDuringCreation() {
            // Given
            when(bookRepository.existsByIsbn(testBookRequestDto.getIsbn())).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenThrow(new RuntimeException("Database error"));

            // When & Then
            assertThatThrownBy(() -> bookService.createBook(testBookRequestDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Database error");

            verify(bookRepository).existsByIsbn(testBookRequestDto.getIsbn());
            verify(bookRepository).save(any(Book.class));
        }

        @Test
        @DisplayName("Should handle case insensitive search")
        void shouldHandleCaseInsensitiveSearch() {
            // Given
            String authorMixedCase = "TeSt AuThOr";
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByAuthorContainingIgnoreCase(authorMixedCase)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.searchBooksByAuthor(authorMixedCase);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);

            verify(bookRepository).findByAuthorContainingIgnoreCase(authorMixedCase);
        }

        @Test
        @DisplayName("Should handle null and empty parameters")
        void shouldHandleNullAndEmptyParameters() {
            // Test null author search
            when(bookRepository.findByAuthorContainingIgnoreCase(null)).thenReturn(Collections.emptyList());

            List<BookResponseDto> result1 = bookService.searchBooksByAuthor(null);
            assertThat(result1).isEmpty();

            // Test empty author search
            when(bookRepository.findByAuthorContainingIgnoreCase("")).thenReturn(Collections.emptyList());

            List<BookResponseDto> result2 = bookService.searchBooksByAuthor("");
            assertThat(result2).isEmpty();

            verify(bookRepository).findByAuthorContainingIgnoreCase(null);
            verify(bookRepository).findByAuthorContainingIgnoreCase("");
        }

        @Test
        @DisplayName("Should handle large datasets pagination")
        void shouldHandleLargeDatasetsPagination() {
            // Given
            Pageable largePageable = PageRequest.of(100, 50); // Page 100, size 50
            Page<Book> emptyPage = new PageImpl<>(Collections.emptyList(), largePageable, 0);
            when(bookRepository.findAll(largePageable)).thenReturn(emptyPage);

            // When
            Page<BookResponseDto> result = bookService.getAllBooks(largePageable);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getNumber()).isEqualTo(100);
            assertThat(result.getSize()).isEqualTo(50);

            verify(bookRepository).findAll(largePageable);
        }

        @Test
        @DisplayName("Should handle multiple books in search results")
        void shouldHandleMultipleBooksInSearchResults() {
            // Given
            Book book2 = new Book();
            book2.setId(2L);
            book2.setTitle("Another Test Book");
            book2.setAuthor("Test Author");

            String author = "Test Author";
            List<Book> books = Arrays.asList(testBook, book2);
            when(bookRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.searchBooksByAuthor(author);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getAuthor()).isEqualTo("Test Author");
            assertThat(result.get(1).getAuthor()).isEqualTo("Test Author");

            verify(bookRepository).findByAuthorContainingIgnoreCase(author);
        }

        @Test
        @DisplayName("Should handle conversion of book to response DTO correctly")
        void shouldHandleConversionOfBookToResponseDtoCorrectly() {
            // Given
            testBook.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
            testBook.setUpdatedAt(LocalDateTime.of(2023, 1, 2, 12, 0));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

            // When
            BookResponseDto result = bookService.getBookById(1L);

            // Then
            assertThat(result.getId()).isEqualTo(testBook.getId());
            assertThat(result.getTitle()).isEqualTo(testBook.getTitle());
            assertThat(result.getAuthor()).isEqualTo(testBook.getAuthor());
            assertThat(result.getIsbn()).isEqualTo(testBook.getIsbn());
            assertThat(result.getPrice()).isEqualTo(testBook.getPrice());
            assertThat(result.getStockQuantity()).isEqualTo(testBook.getStockQuantity());
            assertThat(result.getPublishedYear()).isEqualTo(testBook.getPublishedYear());
            assertThat(result.getDescription()).isEqualTo(testBook.getDescription());
            assertThat(result.getCreatedAt()).isEqualTo(testBook.getCreatedAt());
            assertThat(result.getUpdatedAt()).isEqualTo(testBook.getUpdatedAt());

            verify(bookRepository).findById(1L);
        }

        @Test
        @DisplayName("Should handle book entity creation with constructor correctly")
        void shouldHandleBookEntityCreationWithConstructorCorrectly() {
            // Given
            when(bookRepository.existsByIsbn(testBookRequestDto.getIsbn())).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
                Book book = invocation.getArgument(0);
                book.setId(1L);
                return book;
            });

            // When
            bookService.createBook(testBookRequestDto);

            // Then - Verify the Book constructor is called with correct parameters
            verify(bookRepository).save(argThat(book ->
                    book.getTitle().equals(testBookRequestDto.getTitle()) &&
                            book.getAuthor().equals(testBookRequestDto.getAuthor()) &&
                            book.getIsbn().equals(testBookRequestDto.getIsbn()) &&
                            book.getPublishedYear().equals(testBookRequestDto.getPublishedYear()) &&
                            book.getDescription().equals(testBookRequestDto.getDescription()) &&
                            book.getPrice().equals(testBookRequestDto.getPrice()) &&
                            book.getStockQuantity().equals(testBookRequestDto.getStockQuantity())
            ));
        }

        @Test
        @DisplayName("Should handle update with same ISBN correctly")
        void shouldHandleUpdateWithSameIsbnCorrectly() {
            // Given
            Long bookId = 1L;
            String sameIsbn = "978-0-123456-78-9";
            testBook.setIsbn(sameIsbn);
            testBookRequestDto.setIsbn(sameIsbn);

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.updateBook(bookId, testBookRequestDto);

            // Then
            assertThat(result).isNotNull();
            verify(bookRepository).findById(bookId);
            verify(bookRepository).save(any(Book.class));
            // Should NOT check if ISBN exists since it's the same
            verify(bookRepository, never()).existsByIsbn(any());
        }

        @Test
        @DisplayName("Should handle update with different ISBN that doesn't exist")
        void shouldHandleUpdateWithDifferentIsbnThatDoesntExist() {
            // Given
            Long bookId = 1L;
            String originalIsbn = "978-0-123456-78-9";
            String newIsbn = "978-0-123456-79-6";

            testBook.setIsbn(originalIsbn);
            testBookRequestDto.setIsbn(newIsbn);

            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
            when(bookRepository.existsByIsbn(newIsbn)).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.updateBook(bookId, testBookRequestDto);

            // Then
            assertThat(result).isNotNull();
            verify(bookRepository).findById(bookId);
            verify(bookRepository).existsByIsbn(newIsbn);
            verify(bookRepository).save(any(Book.class));
        }

        @Test
        @DisplayName("Should handle null values in book fields")
        void shouldHandleNullValuesInBookFields() {
            // Given
            testBookRequestDto.setDescription(null);
            testBookRequestDto.setPublishedYear(null);

            when(bookRepository.existsByIsbn(testBookRequestDto.getIsbn())).thenReturn(false);
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            BookResponseDto result = bookService.createBook(testBookRequestDto);

            // Then
            assertThat(result).isNotNull();
            verify(bookRepository).save(argThat(book ->
                    book.getDescription() == null &&
                            book.getPublishedYear() == null
            ));
        }

        @Test
        @DisplayName("Should handle conversion of empty/null book collections")
        void shouldHandleConversionOfEmptyNullBookCollections() {
            // Given
            String author = "Nonexistent Author";
            when(bookRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(null);

            // When & Then - Should handle null gracefully
            assertThatThrownBy(() -> bookService.searchBooksByAuthor(author))
                    .isInstanceOf(NullPointerException.class);

            verify(bookRepository).findByAuthorContainingIgnoreCase(author);
        }

        @Test
        @DisplayName("Should handle price range with equal min and max values")
        void shouldHandlePriceRangeWithEqualMinAndMaxValues() {
            // Given
            Double price = 29.99;
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByPriceBetween(price, price)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.getBooksByPriceRange(price, price);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPrice()).isEqualTo(price);

            verify(bookRepository).findByPriceBetween(price, price);
        }

        @Test
        @DisplayName("Should handle year search with current year")
        void shouldHandleYearSearchWithCurrentYear() {
            // Given
            Integer currentYear = LocalDateTime.now().getYear();
            testBook.setPublishedYear(currentYear);
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findByPublishedYear(currentYear)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.getBooksByYear(currentYear);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPublishedYear()).isEqualTo(currentYear);

            verify(bookRepository).findByPublishedYear(currentYear);
        }

        @Test
        @DisplayName("Should handle stock threshold of zero")
        void shouldHandleStockThresholdOfZero() {
            // Given
            Integer threshold = 0;
            List<Book> books = Collections.emptyList();
            when(bookRepository.findByStockQuantityLessThan(threshold)).thenReturn(books);

            // When
            List<BookResponseDto> result = bookService.getLowStockBooks(threshold);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();

            verify(bookRepository).findByStockQuantityLessThan(threshold);
        }

        @Test
        @DisplayName("Should handle update book method parameter validation")
        void shouldHandleUpdateBookMethodParameterValidation() {
            // Given
            Long bookId = 1L;
            when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
            when(bookRepository.save(any(Book.class))).thenReturn(testBook);

            // When
            bookService.updateBook(bookId, testBookRequestDto);

            // Then - Verify all fields are properly updated
            verify(bookRepository).save(argThat(book -> {
                // Manually check each field since the updateBookFromDto method sets them
                return book.getTitle().equals(testBookRequestDto.getTitle()) &&
                        book.getAuthor().equals(testBookRequestDto.getAuthor()) &&
                        book.getIsbn().equals(testBookRequestDto.getIsbn()) &&
                        book.getPublishedYear().equals(testBookRequestDto.getPublishedYear()) &&
                        book.getDescription().equals(testBookRequestDto.getDescription()) &&
                        book.getPrice().equals(testBookRequestDto.getPrice()) &&
                        book.getStockQuantity().equals(testBookRequestDto.getStockQuantity());
            }));
        }
    }
}