package com.richardvinz.Book_Management_App.controller;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
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

import static com.richardvinz.Book_Management_App.testUtil.TestDataBuilder.createValidBookRequestDto;
import static com.richardvinz.Book_Management_App.testUtil.TestDataBuilder.createValidBookResponseDto;
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
        validBookRequest = createValidBookRequestDto();
        bookResponse = createValidBookResponseDto();
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
                .andExpect(jsonPath("$.id").value(bookResponse.getId()))
                .andExpect(jsonPath("$.title").value(bookResponse.getTitle()))
                .andExpect(jsonPath("$.author").value(bookResponse.getAuthor()))
                .andExpect(jsonPath("$.isbn").value(bookResponse.getIsbn()))
                .andExpect(jsonPath("$.publishedYear").value(bookResponse.getPublishedYear()))
                .andExpect(jsonPath("$.description").value(bookResponse.getDescription()))
                .andExpect(jsonPath("$.price").value(bookResponse.getPrice()))
                .andExpect(jsonPath("$.stockQuantity").value(bookResponse.getStockQuantity()));

        verify(bookService).createBook(any(BookRequestDto.class));
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
    @DisplayName("Should get low stock books")
    void shouldGetLowStockBooks() throws Exception {
        // Given
        Integer threshold = 10;
        BookResponseDto lowStockBook = createValidBookResponseDto();
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
}