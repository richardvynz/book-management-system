package com.richardvinz.Book_Management_App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richardvinz.Book_Management_App.BookManagementAppApplication;
import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import com.richardvinz.Book_Management_App.entity.Book;
import com.richardvinz.Book_Management_App.repository.BookRepository;
import com.richardvinz.Book_Management_App.testUtil.TestDataBuilder;
import com.richardvinz.Book_Management_App.testUtil.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BookManagementAppApplication.class)
@ActiveProfiles("test")
@Transactional
@DisplayName("Book Controller Integration Tests")
class BookControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Manually configure MockMvc
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("Should perform complete CRUD operations")
    void shouldPerformCompleteCrudOperations() throws Exception {
        // CREATE - Add a new book
        BookRequestDto newBook = TestDataBuilder.createValidBookRequestDto();

        String createResponse = mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(newBook)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test Book")))
                .andExpect(jsonPath("$.author", is("Test Author")))
                .andReturn().getResponse().getContentAsString();

        // Extract ID from create response properly
        BookResponseDto createdBook = objectMapper.readValue(createResponse, BookResponseDto.class);
        Long bookId = createdBook.getId();

        // READ - Get the created book
        mockMvc.perform(get("/api/v1/books/{id}", bookId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookId.intValue())))
                .andExpect(jsonPath("$.title", is("Test Book")));

        // UPDATE - Modify the book
        newBook.setTitle("Updated Test Book");
        newBook.setPrice(35.99);

        mockMvc.perform(put("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(newBook)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Test Book")))
                .andExpect(jsonPath("$.price", is(35.99)));

        // DELETE - Remove the book
        mockMvc.perform(delete("/api/v1/books/{id}", bookId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify deletion
        mockMvc.perform(get("/api/v1/books/{id}", bookId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle search operations")
    void shouldHandleSearchOperations() throws Exception {
        // Setup test data
        Book book1 = TestDataBuilder.createBook(null, "The Great Gatsby", "F. Scott Fitzgerald", "978-0-123456-78-9");
        Book book2 = TestDataBuilder.createBook(null, "To Kill a Mockingbird", "Harper Lee", "978-0-123456-79-6");
        Book book3 = TestDataBuilder.createBook(null, "1984", "George Orwell", "978-0-123456-80-2");

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        // Search by author
        mockMvc.perform(get("/api/v1/books/search/author")
                        .param("author", "Fitzgerald"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("The Great Gatsby")));

        // Search by title
        mockMvc.perform(get("/api/v1/books/search/title")
                        .param("title", "1984"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is("George Orwell")));

        // Search by keyword
        mockMvc.perform(get("/api/v1/books/search")
                        .param("keyword", "Kill")
                        .param("page", "0")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", containsString("Mockingbird")));
    }

    @Test
    @DisplayName("Should handle validation errors")
    void shouldHandleValidationErrors() throws Exception {
        BookRequestDto invalidBook = TestDataBuilder.createInvalidBookRequestDto();

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
    @DisplayName("Should handle duplicate ISBN")
    void shouldHandleDuplicateIsbn() throws Exception {
        // Create first book
        BookRequestDto book1 = TestDataBuilder.createValidBookRequestDto();

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(book1)))
                .andExpect(status().isCreated());

        // Try to create second book with same ISBN
        BookRequestDto book2 = TestDataBuilder.createBookRequestDto("Different Title", "Different Author", book1.getIsbn());

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(book2)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation Error")))
                .andExpect(jsonPath("$.details", containsString("already exists")));
    }
}