package com.richardvinz.Book_Management_App.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Book request data transfer object")
public class BookRequestDto {

    @Schema(description = "Book title", example = "The Great Gatsby", required = true)
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @Schema(description = "Book author", example = "F. Scott Fitzgerald", required = true)
    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;

    @Schema(description = "Book ISBN", example = "978-0-7432-7356-5", required = true)
    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
            message = "Invalid ISBN format")
    private String isbn;

    @Schema(description = "Year the book was published", example = "1925")
    @Min(value = 1000, message = "Published year must be at least 1000")
    @Max(value = 2024, message = "Published year cannot be in the future")
    private Integer publishedYear;

    @Schema(description = "Book description", example = "A classic American novel")
    private String description;

    @Schema(description = "Book price", example = "29.99", required = true)
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @NotNull(message = "Price is required")
    private Double price;

    @Schema(description = "Stock quantity", example = "100", required = true)
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;

    // Constructors
    public BookRequestDto() {}

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}