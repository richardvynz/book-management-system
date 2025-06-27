package com.richardvinz.Book_Management_App.service;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import com.richardvinz.Book_Management_App.entity.Book;
import com.richardvinz.Book_Management_App.exception.BookNotFoundException;
import com.richardvinz.Book_Management_App.exception.ValidationException;
import com.richardvinz.Book_Management_App.repository.BookRepository;
import com.richardvinz.Book_Management_App.service.impl.BookServiceImpl;
import com.richardvinz.Book_Management_App.testUtil.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Book Service Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookRequestDto testBookRequestDto;

    @BeforeEach
    void setUp() {
        testBook = TestDataBuilder.createValidBook();
        testBookRequestDto = TestDataBuilder.createValidBookRequestDto();
    }

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
        assertThat(result.getTitle()).isEqualTo(testBookRequestDto.getTitle());
        assertThat(result.getAuthor()).isEqualTo(testBookRequestDto.getAuthor());
        assertThat(result.getIsbn()).isEqualTo(testBookRequestDto.getIsbn());

        verify(bookRepository).existsByIsbn(testBookRequestDto.getIsbn());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should throw exception when creating book with duplicate ISBN")
    void shouldThrowExceptionWhenCreatingBookWithDuplicateISBN() {
        // Given
        when(bookRepository.existsByIsbn(testBookRequestDto.getIsbn())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> bookService.createBook(testBookRequestDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Book with ISBN " + testBookRequestDto.getIsbn() + " already exists");

        verify(bookRepository).existsByIsbn(testBookRequestDto.getIsbn());
        verify(bookRepository, never()).save(any(Book.class));
    }

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
        assertThat(result.getTitle()).isEqualTo(testBook.getTitle());

        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("Should throw exception when book not found")
    void shouldThrowExceptionWhenBookNotFound() {
        // Given
        Long bookId = 999L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bookService.getBookById(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found with ID: " + bookId);

        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("Should get all books with pagination")
    void shouldGetAllBooksWithPagination() {
        // Given
        List<Book> books = Arrays.asList(testBook, TestDataBuilder.createBook(2L, "Book 2", "Author 2", "978-0-123456-79-6"));
        Page<Book> bookPage = new PageImpl<>(books);
        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        // When
        Page<BookResponseDto> result = bookService.getAllBooks(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Book");
        assertThat(result.getContent().get(1).getTitle()).isEqualTo("Book 2");

        verify(bookRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should update book successfully")
    void shouldUpdateBookSuccessfully() {
        // Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        testBookRequestDto.setTitle("Updated Title");

        // When
        BookResponseDto result = bookService.updateBook(bookId, testBookRequestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");

        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    @DisplayName("Should delete book successfully")
    void shouldDeleteBookSuccessfully() {
        // Given
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // When
        bookService.deleteBook(bookId);

        // Then
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent book")
    void shouldThrowExceptionWhenDeletingNonExistentBook() {
        // Given
        Long bookId = 999L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> bookService.deleteBook(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book not found with ID: " + bookId);

        verify(bookRepository).existsById(bookId);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should search books by author")
    void shouldSearchBooksByAuthor() {
        // Given
        String author = "Test Author";
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findByAuthorContainingIgnoreCase(author)).thenReturn(books);

        // When
        List<BookResponseDto> result = bookService.searchBooksByAuthor(author);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo(author);

        verify(bookRepository).findByAuthorContainingIgnoreCase(author);
    }

    @Test
    @DisplayName("Should get low stock books")
    void shouldGetLowStockBooks() {
        // Given
        Integer threshold = 10;
        Book lowStockBook = TestDataBuilder.createBook(2L, "Low Stock Book", "Author", "978-0-123456-80-2");
        lowStockBook.setStockQuantity(5);
        List<Book> books = Arrays.asList(lowStockBook);

        when(bookRepository.findByStockQuantityLessThan(threshold)).thenReturn(books);

        // When
        List<BookResponseDto> result = bookService.getLowStockBooks(threshold);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStockQuantity()).isLessThan(threshold);

        verify(bookRepository).findByStockQuantityLessThan(threshold);
    }
}