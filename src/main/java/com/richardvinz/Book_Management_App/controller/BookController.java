package com.richardvinz.Book_Management_App.controller;

import com.richardvinz.Book_Management_App.dto.BookRequestDto;
import com.richardvinz.Book_Management_App.dto.BookResponseDto;
import com.richardvinz.Book_Management_App.dto.ErrorResponseDto;
import com.richardvinz.Book_Management_App.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Validated
@Tag(name = "Book Management", description = "API for managing books in the library system")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Create a new book", description = "Creates a new book in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Book with ISBN already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<BookResponseDto> createBook(
            @Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto createdBook = bookService.createBook(bookRequestDto);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all books", description = "Retrieves all books with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) int size,

            @Parameter(description = "Sort field", example = "title")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BookResponseDto> books = bookService.getAllBooks(pageable);

        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get book by ID", description = "Retrieves a specific book by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(
            @Parameter(description = "Book ID", example = "1", required = true)
            @PathVariable @Min(1) Long id) {
        BookResponseDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Update book", description = "Updates an existing book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "ISBN already exists",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> updateBook(
            @Parameter(description = "Book ID", example = "1", required = true)
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto updatedBook = bookService.updateBook(id, bookRequestDto);
        return ResponseEntity.ok(updatedBook);
    }

    @Operation(summary = "Delete book", description = "Deletes a book from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "Book ID", example = "1", required = true)
            @PathVariable @Min(1) Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search books by author", description = "Searches for books by author name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/search/author")
    public ResponseEntity<List<BookResponseDto>> searchBooksByAuthor(
            @Parameter(description = "Author name", example = "Shakespeare", required = true)
            @RequestParam String author) {
        List<BookResponseDto> books = bookService.searchBooksByAuthor(author);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Search books by title", description = "Searches for books by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/search/title")
    public ResponseEntity<List<BookResponseDto>> searchBooksByTitle(
            @Parameter(description = "Book title", example = "Hamlet", required = true)
            @RequestParam String title) {
        List<BookResponseDto> books = bookService.searchBooksByTitle(title);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Search books by keyword", description = "Searches for books by keyword in title, author, or description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<Page<BookResponseDto>> searchBooksByKeyword(
            @Parameter(description = "Search keyword", example = "fantasy", required = true)
            @RequestParam String keyword,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BookResponseDto> books = bookService.searchBooksByKeyword(keyword, pageable);

        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get books by publication year", description = "Retrieves books published in a specific year")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/year/{year}")
    public ResponseEntity<List<BookResponseDto>> getBooksByYear(
            @Parameter(description = "Publication year", example = "2020", required = true)
            @PathVariable @Min(1000) Integer year) {
        List<BookResponseDto> books = bookService.getBooksByYear(year);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get books by price range", description = "Retrieves books within a specific price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/price-range")
    public ResponseEntity<List<BookResponseDto>> getBooksByPriceRange(
            @Parameter(description = "Minimum price", example = "10.0", required = true)
            @RequestParam Double minPrice,

            @Parameter(description = "Maximum price", example = "50.0", required = true)
            @RequestParam Double maxPrice) {
        List<BookResponseDto> books = bookService.getBooksByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Get low stock books", description = "Retrieves books with stock quantity below threshold")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Low stock books found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)))
    })
    @GetMapping("/low-stock")
    public ResponseEntity<List<BookResponseDto>> getLowStockBooks(
            @Parameter(description = "Stock threshold", example = "10", required = true)
            @RequestParam @Min(0) Integer threshold) {
        List<BookResponseDto> books = bookService.getLowStockBooks(threshold);
        return ResponseEntity.ok(books);
    }
}