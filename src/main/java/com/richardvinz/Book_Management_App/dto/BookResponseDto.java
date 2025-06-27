package com.richardvinz.Book_Management_App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
}