package com.richardvinz.Book_Management_App.e2e;

import com.richardvinz.Book_Management_App.BookManagementAppApplication;
import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.testUtil.TestDataBuilder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(
        classes = BookManagementAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Book Management E2E Tests")
class BookManagementE2ETest {

    @LocalServerPort
    private int port;

    // Fix: Change to Integer to match JSON response type
    private static Integer createdBookId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Order(1)
    @DisplayName("E2E: Should create a new book")
    void shouldCreateNewBook() {
        BookRequestDto newBook = TestDataBuilder.createValidBookRequestDto();

        // Fix: Extract as Integer, not Long
        createdBookId = given()
                .contentType(ContentType.JSON)
                .body(newBook)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(201)
                .body("title", equalTo("Test Book"))
                .body("author", equalTo("Test Author"))
                .body("isbn", equalTo("978-0-123456-78-9"))
                .body("price", equalTo(29.99f))
                .body("stockQuantity", equalTo(100))
                .body("id", notNullValue())
                .extract()
                .path("id");

        System.out.println("Created book with ID: " + createdBookId);
    }

    @Test
    @Order(2)
    @DisplayName("E2E: Should retrieve the created book")
    void shouldRetrieveCreatedBook() {
        // Fix: Add null check and better error handling
        Assumptions.assumeTrue(createdBookId != null, "Book ID is null - creation test may have failed");

        given()
                .when()
                .get("/api/v1/books/{id}", createdBookId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdBookId))
                .body("title", equalTo("Test Book"))
                .body("author", equalTo("Test Author"));
    }

    @Test
    @Order(3)
    @DisplayName("E2E: Should list all books with pagination")
    void shouldListAllBooksWithPagination() {
        given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "title")
                .queryParam("sortDir", "asc")
                .when()
                .get("/api/v1/books")
                .then()
                .statusCode(200)
                .body("content", hasSize(greaterThanOrEqualTo(1)))
                .body("content[0].title", equalTo("Test Book"))
                .body("totalElements", greaterThanOrEqualTo(1))
                .body("totalPages", greaterThanOrEqualTo(1));
    }

    @Test
    @Order(4)
    @DisplayName("E2E: Should update the book")
    void shouldUpdateBook() {
        // Fix: Add null check
        Assumptions.assumeTrue(createdBookId != null, "Book ID is null - creation test may have failed");

        BookRequestDto updatedBook = TestDataBuilder.createValidBookRequestDto();
        updatedBook.setTitle("Updated Test Book");
        updatedBook.setPrice(35.99);
        updatedBook.setStockQuantity(150);

        given()
                .contentType(ContentType.JSON)
                .body(updatedBook)
                .when()
                .put("/api/v1/books/{id}", createdBookId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdBookId))
                .body("title", equalTo("Updated Test Book"))
                .body("price", equalTo(35.99f))
                .body("stockQuantity", equalTo(150));
    }

    @Test
    @Order(5)
    @DisplayName("E2E: Should search books by author")
    void shouldSearchBooksByAuthor() {
        given()
                .queryParam("author", "Test Author")
                .when()
                .get("/api/v1/books/search/author")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(1)))
                .body("[0].author", equalTo("Test Author"));
    }

    @Test
    @Order(6)
    @DisplayName("E2E: Should search books by keyword")
    void shouldSearchBooksByKeyword() {
        // Fix: Use the updated title from previous test
        given()
                .queryParam("keyword", "Updated")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/books/search")
                .then()
                .statusCode(200)
                .body("content", hasSize(greaterThanOrEqualTo(1)))
                .body("content[0].title", containsString("Updated"));
    }

    @Test
    @Order(7)
    @DisplayName("E2E: Should handle validation errors")
    void shouldHandleValidationErrors() {
        BookRequestDto invalidBook = TestDataBuilder.createInvalidBookRequestDto();

        given()
                .contentType(ContentType.JSON)
                .body(invalidBook)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(400)
                .body("status", equalTo(400))
                .body("message", equalTo("Validation Failed"))
                .body("validationErrors", notNullValue());
    }

    @Test
    @Order(8)
    @DisplayName("E2E: Should handle not found errors")
    void shouldHandleNotFoundErrors() {
        given()
                .when()
                .get("/api/v1/books/99999")
                .then()
                .statusCode(404)
                .body("status", equalTo(404))
                .body("message", equalTo("Book Not Found"))
                .body("details", containsString("Book not found with ID: 99999"));
    }

    @Test
    @Order(9)
    @DisplayName("E2E: Should prevent duplicate ISBN")
    void shouldPreventDuplicateIsbn() {
        BookRequestDto duplicateBook = TestDataBuilder.createBookRequestDto(
                "Another Book",
                "Another Author",
                "978-0-123456-78-9" // Same ISBN as the first book
        );

        given()
                .contentType(ContentType.JSON)
                .body(duplicateBook)
                .when()
                .post("/api/v1/books")
                .then()
                .statusCode(400)
                .body("message", equalTo("Validation Error"))
                .body("details", containsString("already exists"));
    }

    @Test
    @Order(10)
    @DisplayName("E2E: Should delete the book")
    void shouldDeleteBook() {
        // Fix: Add null check before deletion
        Assumptions.assumeTrue(createdBookId != null, "Book ID is null - creation test may have failed");

        given()
                .when()
                .delete("/api/v1/books/{id}", createdBookId)
                .then()
                .statusCode(204);

        // Verify deletion
        given()
                .when()
                .get("/api/v1/books/{id}", createdBookId)
                .then()
                .statusCode(404);
    }

    @Test
    @Order(11)
    @DisplayName("E2E: Should perform complex search scenario")
    void shouldPerformComplexSearchScenario() {
        // Create multiple books for search testing
        BookRequestDto book1 = TestDataBuilder.createBookRequestDto("Java Programming", "John Doe", "978-1-111111-11-1");
        BookRequestDto book2 = TestDataBuilder.createBookRequestDto("Python Guide", "Jane Smith", "978-2-222222-22-2");
        BookRequestDto book3 = TestDataBuilder.createBookRequestDto("JavaScript Basics", "John Doe", "978-3-333333-33-3");

        // Create the books and store their IDs for cleanup
        Integer book1Id = given().contentType(ContentType.JSON).body(book1).post("/api/v1/books")
                .then().statusCode(201).extract().path("id");

        Integer book2Id = given().contentType(ContentType.JSON).body(book2).post("/api/v1/books")
                .then().statusCode(201).extract().path("id");

        Integer book3Id = given().contentType(ContentType.JSON).body(book3).post("/api/v1/books")
                .then().statusCode(201).extract().path("id");

        // Search by author
        given()
                .queryParam("author", "John Doe")
                .when()
                .get("/api/v1/books/search/author")
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("title", hasItems("Java Programming", "JavaScript Basics"));

        // Search by title
        given()
                .queryParam("title", "Python")
                .when()
                .get("/api/v1/books/search/title")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].title", equalTo("Python Guide"));

        // Search by keyword with pagination
        given()
                .queryParam("keyword", "Java")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/books/search")
                .then()
                .statusCode(200)
                .body("content", hasSize(2));

        // Cleanup: Delete the created books
        if (book1Id != null) {
            given().delete("/api/v1/books/{id}", book1Id).then().statusCode(204);
        }
        if (book2Id != null) {
            given().delete("/api/v1/books/{id}", book2Id).then().statusCode(204);
        }
        if (book3Id != null) {
            given().delete("/api/v1/books/{id}", book3Id).then().statusCode(204);
        }
    }
}