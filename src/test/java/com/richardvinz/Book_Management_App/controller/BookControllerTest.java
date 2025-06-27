package com.richardvinz.Book_Management_App.controller;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import com.richardvinz.Book_Management_App.exception.BookNotFoundException;
import com.richardvinz.Book_Management_App.exception.ValidationException;
import com.richardvinz.Book_Management_App.service.BookService;
import com.richardvinz.Book_Management_App.testUtil.TestDataBuilder;
import com.richardvinz.Book_Management_App.testUtil.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@DisplayName("Book Controller Tests")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private BookRequestDto validBookRequest;
    private BookResponseDto bookResponse;

    @BeforeEach
    void setUp() {
        validBookRequest = TestDataBuilder.createValidBookRequestDto();
        bookResponse = TestDataBuilder.createValidBookResponseDto();
    }

    @Test
    @DisplayName("Should create book successfully")
    void shouldCreateBookSuccessfully() throws Exception {
        // Given
        when(bookService.createBook(any(BookRequestDto.class))).thenReturn(bookResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(validBookRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Book")))
                .andExpect(jsonPath("$.author", is("Test Author")))
                .andExpect(jsonPath("$.isbn", is("978-0-123456-78-9")))
                .andExpect(jsonPath("$.price", is(29.99)))
                .andExpect(jsonPath("$.stockQuantity", is(100)));

        verify(bookService).createBook(any(BookRequestDto.class));
    }

    @Test
    @DisplayName("Should return bad request for invalid book data")
    void shouldReturnBadRequestForInvalidBookData() throws Exception {
        // Given
        BookRequestDto invalidRequest = TestDataBuilder.createInvalidBookRequestDto();

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation Failed")))
                .andExpect(jsonPath("$.validationErrors", hasSize(greaterThan(0))));

        verify(bookService, never()).createBook(any(BookRequestDto.class));
    }

    @Test
    @DisplayName("Should get book by ID successfully")
    void shouldGetBookByIdSuccessfully() throws Exception {
        // Given
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenReturn(bookResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/books/{id}", bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Book")))
                .andExpect(jsonPath("$.author", is("Test Author")));

        verify(bookService).getBookById(bookId);
    }

    @Test
    @DisplayName("Should return not found for non-existent book")
    void shouldReturnNotFoundForNonExistentBook() throws Exception {
        // Given
        Long bookId = 999L;
        when(bookService.getBookById(bookId))
                .thenThrow(new BookNotFoundException("Book not found with ID: " + bookId));

        // When & Then
        mockMvc.perform(get("/api/v1/books/{id}", bookId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Book Not Found")))
                .andExpect(jsonPath("$.details", containsString("Book not found with ID: 999")));

        verify(bookService).getBookById(bookId);
    }

    @Test
    @DisplayName("Should get all books with pagination")
    void shouldGetAllBooksWithPagination() throws Exception {
        // Given
        List<BookResponseDto> books = Arrays.asList(bookResponse);
        Page<BookResponseDto> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);

        when(bookService.getAllBooks(any())).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/v1/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "title")
                        .param("sortDir", "asc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Test Book")))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.number", is(0)));

        verify(bookService).getAllBooks(any());
    }

    @Test
    @DisplayName("Should update book successfully")
    void shouldUpdateBookSuccessfully() throws Exception {
        // Given
        Long bookId = 1L;
        validBookRequest.setTitle("Updated Title");
        bookResponse.setTitle("Updated Title");

        when(bookService.updateBook(eq(bookId), any(BookRequestDto.class))).thenReturn(bookResponse);

        // When & Then
        mockMvc.perform(put("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(validBookRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Updated Title")));

        verify(bookService).updateBook(eq(bookId), any(BookRequestDto.class));
    }

    @Test
    @DisplayName("Should delete book successfully")
    void shouldDeleteBookSuccessfully() throws Exception {
        // Given
        Long bookId = 1L;
        doNothing().when(bookService).deleteBook(bookId);

        // When & Then
        mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(bookId);
    }

    @Test
    @DisplayName("Should search books by author")
    void shouldSearchBooksByAuthor() throws Exception {
        // Given
        String author = "Test Author";
        List<BookResponseDto> books = Arrays.asList(bookResponse);

        when(bookService.searchBooksByAuthor(author)).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/v1/books/search/author")
                        .param("author", author))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is(author)));

        verify(bookService).searchBooksByAuthor(author);
    }

    @Test
    @DisplayName("Should search books by title")
    void shouldSearchBooksByTitle() throws Exception {
        // Given
        String title = "Test Book";
        List<BookResponseDto> books = Arrays.asList(bookResponse);

        when(bookService.searchBooksByTitle(title)).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/v1/books/search/title")
                        .param("title", title))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(title)));

        verify(bookService).searchBooksByTitle(title);
    }

    @Test
    @DisplayName("Should search books by keyword")
    void shouldSearchBooksByKeyword() throws Exception {
        // Given
        String keyword = "test";
        List<BookResponseDto> books = Arrays.asList(bookResponse);
        Page<BookResponseDto> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);

        when(bookService.searchBooksByKeyword(eq(keyword), any())).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/v1/books/search")
                        .param("keyword", keyword)
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", containsString("Test")));

        verify(bookService).searchBooksByKeyword(eq(keyword), any());
    }

    @Test
    @DisplayName("Should get books by publication year")
    void shouldGetBooksByYear() throws Exception {
        // Given
        Integer year = 2023;
        List<BookResponseDto> books = Arrays.asList(bookResponse);

        when(bookService.getBooksByYear(year)).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/v1/books/year/{year}", year))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].publishedYear", is(year)));

        verify(bookService).getBooksByYear(year);
    }

    @Test
    @DisplayName("Should get books by price range")
    void shouldGetBooksByPriceRange() throws Exception {
        // Given
        Double minPrice = 10.0;
        Double maxPrice = 50.0;
        List<BookResponseDto> books = Arrays.asList(bookResponse);

        when(bookService.getBooksByPriceRange(minPrice, maxPrice)).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/v1/books/price-range")
                        .param("minPrice", minPrice.toString())
                        .param("maxPrice", maxPrice.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price", is(29.99)));

        verify(bookService).getBooksByPriceRange(minPrice, maxPrice);
    }

    @Test
    @DisplayName("Should get low stock books")
    void shouldGetLowStockBooks() throws Exception {
        // Given
        Integer threshold = 10;
        BookResponseDto lowStockBook = TestDataBuilder.createValidBookResponseDto();
        lowStockBook.setStockQuantity(5);
        List<BookResponseDto> books = Arrays.asList(lowStockBook);

        when(bookService.getLowStockBooks(threshold)).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/api/v1/books/low-stock")
                        .param("threshold", threshold.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].stockQuantity", is(5)));

        verify(bookService).getLowStockBooks(threshold);
    }

    @Test
    @DisplayName("Should handle validation exception on creation")
    void shouldHandleValidationExceptionOnCreation() throws Exception {
        // Given
        when(bookService.createBook(any(BookRequestDto.class)))
                .thenThrow(new ValidationException("Book with ISBN 978-0-123456-78-9 already exists"));

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(validBookRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation Error")))
                .andExpect(jsonPath("$.details", containsString("already exists")));

        verify(bookService).createBook(any(BookRequestDto.class));
    }
}