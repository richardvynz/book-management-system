package com.richardvinz.Book_Management_App.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Error response data transfer object")
public class ErrorResponseDto {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error message", example = "Book not found")
    private String message;

    @Schema(description = "Detailed error description", example = "Book with ID 123 was not found")
    private String details;

    @Schema(description = "Timestamp when error occurred",
            example = "2023-12-01T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Request path where error occurred",
            example = "/api/v1/books/123")
    private String path;

    @Schema(description = "List of validation errors")
    private List<String> validationErrors;

    public ErrorResponseDto() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDto(int status, String message, String details, String path) {
        this();
        this.status = status;
        this.message = message;
        this.details = details;
        this.path = path;
    }

    // Getters and Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public List<String> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
}