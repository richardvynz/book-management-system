package com.richardvinz.Book_Management_App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Book response data transfer object")
public class BookResponseDto {

    @Schema(description = "Book ID", example = "1")
    private Long id;

    @Schema(description = "Book title", example = "The Great Gatsby")
    private String title;

    @Schema(description = "Book author", example = "F. Scott Fitzgerald")
    private String author;

    @Schema(description = "Book ISBN", example = "978-0-7432-7356-5")
    private String isbn;

    @Schema(description = "Year the book was published", example = "1925")
    private Integer publishedYear;

    @Schema(description = "Book description", example = "A classic American novel")
    private String description;

    @Schema(description = "Book price", example = "29.99")
    private Double price;

    @Schema(description = "Stock quantity", example = "100")
    private Integer stockQuantity;

    @Schema(description = "Creation timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2023-12-01T10:30:00")
    private LocalDateTime updatedAt;

    // Constructors
    public BookResponseDto() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}