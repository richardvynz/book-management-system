package com.richardvinz.Book_Management_App.exception;

import com.richardvinz.Book_Management_App.controller.BookController;
import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.service.BookService;
import com.richardvinz.Book_Management_App.testUtil.TestDataBuilder;
import com.richardvinz.Book_Management_App.testUtil.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("GlobalExceptionHandler Tests - Based on Actual Implementation")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Nested
    @DisplayName("BookNotFoundException Tests")
    class BookNotFoundExceptionTests {

        @Test
        @DisplayName("Should handle BookNotFoundException for GET request")
        void shouldHandleBookNotFoundExceptionForGetRequest() throws Exception {
            // Given
            Long bookId = 999L;
            when(bookService.getBookById(bookId))
                    .thenThrow(new BookNotFoundException("Book not found with ID: " + bookId));

            // When & Then - Based on YOUR ErrorResponseDto structure
            mockMvc.perform(get("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", is("Book Not Found")))  // YOUR handler sets this
                    .andExpect(jsonPath("$.details", is("Book not found with ID: 999")))  // YOUR ex.getMessage()
                    .andExpect(jsonPath("$.timestamp", notNullValue()))
                    .andExpect(jsonPath("$.path", is("/api/v1/books/999")));
        }

        @Test
        @DisplayName("Should handle BookNotFoundException for DELETE request")
        void shouldHandleBookNotFoundExceptionForDeleteRequest() throws Exception {
            // Given
            Long bookId = 999L;

            doThrow(new BookNotFoundException("Book not found with ID: " + bookId))
                    .when(bookService).deleteBook(bookId);

            // When & Then
            mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", is("Book Not Found")))
                    .andExpect(jsonPath("$.details", is("Book not found with ID: 999")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()))
                    .andExpect(jsonPath("$.path", is("/api/v1/books/999")));
        }

        @Test
        @DisplayName("Should handle BookNotFoundException with different message")
        void shouldHandleBookNotFoundExceptionWithDifferentMessage() throws Exception {
            // Given
            Long bookId = 123L;
            when(bookService.getBookById(bookId))
                    .thenThrow(new BookNotFoundException("No book exists with the given ID"));

            // When & Then
            mockMvc.perform(get("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", is("Book Not Found")))
                    .andExpect(jsonPath("$.details", is("No book exists with the given ID")))
                    .andExpect(jsonPath("$.path", is("/api/v1/books/123")));
        }
    }

    @Nested
    @DisplayName("ValidationException Tests")
    class ValidationExceptionTests {

        @Test
        @DisplayName("Should handle ValidationException for duplicate ISBN")
        void shouldHandleValidationExceptionForDuplicateIsbn() throws Exception {
            // Given
            BookRequestDto bookRequest = TestDataBuilder.createValidBookRequestDto();
            when(bookService.createBook(any(BookRequestDto.class)))
                    .thenThrow(new ValidationException("Book with ISBN 978-0-123456-78-9 already exists"));

            // When & Then
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(bookRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", is("Validation Error")))  // YOUR handler sets this
                    .andExpect(jsonPath("$.details", is("Book with ISBN 978-0-123456-78-9 already exists")))
                    .andExpect(jsonPath("$.timestamp", notNullValue()))
                    .andExpect(jsonPath("$.path", is("/api/v1/books")));
        }

        @Test
        @DisplayName("Should handle ValidationException for update operation")
        void shouldHandleValidationExceptionForUpdateOperation() throws Exception {
            // Given
            Long bookId = 1L;
            BookRequestDto bookRequest = TestDataBuilder.createValidBookRequestDto();
            when(bookService.updateBook(anyLong(), any(BookRequestDto.class)))
                    .thenThrow(new ValidationException("ISBN is already in use by another book"));

            // When & Then
            mockMvc.perform(put("/api/v1/books/{id}", bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(bookRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", is("Validation Error")))
                    .andExpect(jsonPath("$.details", is("ISBN is already in use by another book")))
                    .andExpect(jsonPath("$.path", is("/api/v1/books/1")));
        }

        @Test
        @DisplayName("Should handle ValidationException with empty message")
        void shouldHandleValidationExceptionWithEmptyMessage() throws Exception {
            // Given
            BookRequestDto bookRequest = TestDataBuilder.createValidBookRequestDto();
            when(bookService.createBook(any(BookRequestDto.class)))
                    .thenThrow(new ValidationException(""));

            // When & Then
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(bookRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", is("Validation Error")))
                    .andExpect(jsonPath("$.details", is("")));
        }

        @Test
        @DisplayName("Should handle ValidationException with null message")
        void shouldHandleValidationExceptionWithNullMessage() throws Exception {
            // Given
            BookRequestDto bookRequest = TestDataBuilder.createValidBookRequestDto();
            when(bookService.createBook(any(BookRequestDto.class)))
                    .thenThrow(new ValidationException((String) null));  // Explicit null

            // When & Then
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(bookRequest)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", is("Validation Error")))
                    .andExpect(jsonPath("$.details").doesNotExist());  // If null, field might not exist in JSON
        }

    @Nested
    @DisplayName("MethodArgumentNotValidException Tests")
    class MethodArgumentNotValidExceptionTests {

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException with validation errors")
        void shouldHandleMethodArgumentNotValidExceptionWithValidationErrors() throws Exception {
            // Given - Create an invalid book request (empty required fields)
            BookRequestDto invalidBook = BookRequestDto.builder()
                    .title("") // Invalid - empty
                    .author("") // Invalid - empty
                    .isbn("invalid-isbn") // Invalid format
                    .price(-10.0) // Invalid - negative
                    .stockQuantity(-5) // Invalid - negative
                    .publishedYear(3000) // Invalid - future year
                    .build();

            // When & Then
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(invalidBook)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", is("Validation Failed")))  // YOUR handler sets this
                    .andExpect(jsonPath("$.details", is("Request validation failed")))  // YOUR handler sets this
                    .andExpect(jsonPath("$.validationErrors", notNullValue()))
                    .andExpect(jsonPath("$.validationErrors", not(empty())))
                    .andExpect(jsonPath("$.timestamp", notNullValue()))
                    .andExpect(jsonPath("$.path", is("/api/v1/books")));
        }

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException with missing required fields")
        void shouldHandleMethodArgumentNotValidExceptionWithMissingFields() throws Exception {
            // Given - Create a book request with null required fields
            BookRequestDto invalidBook = BookRequestDto.builder()
                    .title(null)
                    .author(null)
                    .isbn(null)
                    .price(null)
                    .stockQuantity(null)
                    .build();

            // When & Then
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(invalidBook)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", is("Validation Failed")))
                    .andExpect(jsonPath("$.validationErrors", notNullValue()));
        }

        @Test
        @DisplayName("Should handle MethodArgumentNotValidException for PUT request")
        void shouldHandleMethodArgumentNotValidExceptionForPutRequest() throws Exception {
            // Given
            Long bookId = 1L;
            BookRequestDto invalidBook = BookRequestDto.builder()
                    .title("") // Invalid
                    .author("Test Author")
                    .isbn("978-0-123456-78-9")
                    .price(29.99)
                    .stockQuantity(100)
                    .build();

            // When & Then
            mockMvc.perform(put("/api/v1/books/{id}", bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(invalidBook)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", is("Validation Failed")))
                    .andExpect(jsonPath("$.path", is("/api/v1/books/1")));
        }
    }

    @Nested
    @DisplayName("DataIntegrityViolationException Tests")
    class DataIntegrityViolationExceptionTests {


        @Test
        @DisplayName("Should handle DataIntegrityViolationException for unique constraint")
        void shouldHandleDataIntegrityViolationExceptionForUniqueConstraint() throws Exception {
            // Given
            BookRequestDto bookRequest = TestDataBuilder.createValidBookRequestDto();
            // Use a message that contains "unique" (case-insensitive)
            when(bookService.createBook(any(BookRequestDto.class)))
                    .thenThrow(new DataIntegrityViolationException("Unique constraint violation on isbn"));

            // When & Then
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(bookRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(409)))
                    .andExpect(jsonPath("$.message", is("Data Integrity Violation")))
                    .andExpect(jsonPath("$.details", is("Duplicate entry - resource already exists")))  // Because message contains "unique"
                    .andExpect(jsonPath("$.timestamp", notNullValue()))
                    .andExpect(jsonPath("$.path", is("/api/v1/books")));
        }

        @Test
        @DisplayName("Should handle DataIntegrityViolationException for general constraint")
        void shouldHandleDataIntegrityViolationExceptionForGeneralConstraint() throws Exception {
            // Given
            BookRequestDto bookRequest = TestDataBuilder.createValidBookRequestDto();
            when(bookService.createBook(any(BookRequestDto.class)))
                    .thenThrow(new DataIntegrityViolationException("Foreign key constraint violation"));

            // When & Then
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(bookRequest)))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status", is(409)))
                    .andExpect(jsonPath("$.message", is("Data Integrity Violation")))
                    .andExpect(jsonPath("$.details", is("Data integrity violation")));  // YOUR default message
        }
    }

    @Nested
    @DisplayName("Generic Exception Tests - YOUR Exception Handler Catches All")
    class GenericExceptionTests {

        @Test
        @DisplayName("Should handle generic RuntimeException via Exception handler")
        void shouldHandleGenericRuntimeException() throws Exception {
            // Given
            Long bookId = 1L;
            when(bookService.getBookById(bookId))
                    .thenThrow(new RuntimeException("Database connection failed"));

            // When & Then - YOUR Exception handler catches this
            mockMvc.perform(get("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.message", is("Internal Server Error")))  // YOUR handler sets this
                    .andExpect(jsonPath("$.details", is("An unexpected error occurred")))  // YOUR handler sets this
                    .andExpect(jsonPath("$.timestamp", notNullValue()))
                    .andExpect(jsonPath("$.path", is("/api/v1/books/1")));
        }

        @Test
        @DisplayName("Should handle IllegalArgumentException via Exception handler")
        void shouldHandleIllegalArgumentException() throws Exception {
            // Given - YOUR handler doesn't have specific IllegalArgumentException handler
            Long bookId = 1L;
            when(bookService.getBookById(bookId))
                    .thenThrow(new IllegalArgumentException("Invalid book ID format"));

            // When & Then - Goes to YOUR generic Exception handler
            mockMvc.perform(get("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())  // YOUR Exception handler returns 500
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.message", is("Internal Server Error")))
                    .andExpect(jsonPath("$.details", is("An unexpected error occurred")));
        }

        @Test
        @DisplayName("Should handle NullPointerException via Exception handler")
        void shouldHandleNullPointerException() throws Exception {
            // Given
            Long bookId = 1L;
            when(bookService.getBookById(bookId))
                    .thenThrow(new NullPointerException("Object reference is null"));

            // When & Then - YOUR Exception handler catches this
            mockMvc.perform(get("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.message", is("Internal Server Error")))
                    .andExpect(jsonPath("$.details", is("An unexpected error occurred")));
        }

        @Test
        @DisplayName("Should handle exception with null message")
        void shouldHandleExceptionWithNullMessage() throws Exception {
            // Given
            Long bookId = 1L;
            when(bookService.getBookById(bookId))
                    .thenThrow(new RuntimeException((String) null));

            // When & Then
            mockMvc.perform(get("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.message", is("Internal Server Error")))
                    .andExpect(jsonPath("$.details", is("An unexpected error occurred")));
        }

        @Test
        @DisplayName("Should handle exception in getAllBooks")
        void shouldHandleExceptionInGetAllBooks() throws Exception {
            // Given
            when(bookService.getAllBooks(any()))
                    .thenThrow(new RuntimeException("Service unavailable"));

            // When & Then
            mockMvc.perform(get("/api/v1/books"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.message", is("Internal Server Error")))
                    .andExpect(jsonPath("$.details", is("An unexpected error occurred")))
                    .andExpect(jsonPath("$.path", is("/api/v1/books")));
        }
    }

    @Nested
    @DisplayName("HTTP Error Handling - YOUR Exception Handler Catches These")
    class HttpErrorHandlingTests {

        @Test
        @DisplayName("Should handle method not allowed via Exception handler")
        void shouldHandleMethodNotAllowedError() throws Exception {
            // Given - YOUR handler doesn't have specific HttpRequestMethodNotSupportedException handler
            BookRequestDto bookRequest = TestDataBuilder.createValidBookRequestDto();

            // When & Then - YOUR Exception handler catches this
            mockMvc.perform(patch("/api/v1/books/1")  // PATCH not supported
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(bookRequest)))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());  // YOUR Exception handler returns 500
        }

        @Test
        @DisplayName("Should handle unsupported media type via Exception handler")
        void shouldHandleUnsupportedMediaType() throws Exception {
            // Given
            BookRequestDto bookRequest = TestDataBuilder.createValidBookRequestDto();

            // When & Then - YOUR Exception handler catches this
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_XML)  // XML not supported
                            .content(TestUtils.asJsonString(bookRequest)))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());  // YOUR Exception handler returns 500
        }
    }

    @Nested
    @DisplayName("Error Response Structure Tests")
    class ErrorResponseStructureTests {

        @Test
        @DisplayName("Should have consistent error response structure")
        void shouldHaveConsistentErrorResponseStructure() throws Exception {
            // Given
            Long bookId = 999L;
            when(bookService.getBookById(bookId))
                    .thenThrow(new BookNotFoundException("Book not found"));

            // When & Then - Verify YOUR ErrorResponseDto structure
            mockMvc.perform(get("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.details").exists())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.path").exists())
                    .andExpect(jsonPath("$.status").isNumber())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.details").isString())
                    .andExpect(jsonPath("$.timestamp").isString())
                    .andExpect(jsonPath("$.path").isString());
        }

        @Test
        @DisplayName("Should include validation errors in response when present")
        void shouldIncludeValidationErrorsInResponseWhenPresent() throws Exception {
            // Given - Invalid book request
            BookRequestDto invalidBook = BookRequestDto.builder()
                    .title("") // Invalid
                    .build();

            // When & Then - Check YOUR validation errors structure
            mockMvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(invalidBook)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.validationErrors").exists())
                    .andExpect(jsonPath("$.validationErrors").isArray())
                    .andExpect(jsonPath("$.validationErrors", not(empty())));
        }
    }

    @Nested
    @DisplayName("Multiple Exception Scenarios")
    class MultipleExceptionScenariosTests {

        @Test
        @DisplayName("Should handle ValidationException in delete operation")
        void shouldHandleValidationExceptionInDeleteOperation() throws Exception {
            // Given
            Long bookId = 1L;

            doThrow(new ValidationException("Cannot delete book with active orders"))
                    .when(bookService).deleteBook(bookId);

            // When & Then
            mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.message", is("Validation Error")))
                    .andExpect(jsonPath("$.details", is("Cannot delete book with active orders")));
        }

        @Test
        @DisplayName("Should handle RuntimeException in delete operation")
        void shouldHandleRuntimeExceptionInDeleteOperation() throws Exception {
            // Given
            Long bookId = 1L;

            doThrow(new RuntimeException("Database constraint violation"))
                    .when(bookService).deleteBook(bookId);

            // When & Then
            mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.message", is("Internal Server Error")))
                    .andExpect(jsonPath("$.details", is("An unexpected error occurred")));
        }
    }
}}