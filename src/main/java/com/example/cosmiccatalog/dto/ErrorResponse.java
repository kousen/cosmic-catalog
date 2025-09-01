package com.example.cosmiccatalog.dto;

import java.time.LocalDateTime;

/**
 * Structured error response for API consistency.
 * Provides clients with actionable error information.
 */
public record ErrorResponse(
    String error,
    String message,
    int status,
    LocalDateTime timestamp,
    String path
) {
    
    /**
     * Factory method for creating error responses.
     */
    public static ErrorResponse of(String error, String message, int status, String path) {
        return new ErrorResponse(error, message, status, LocalDateTime.now(), path);
    }
    
    /**
     * Convenience factory for version conflict errors.
     */
    public static ErrorResponse versionConflict(String message, String path) {
        return of("VERSION_CONFLICT", message, 409, path);
    }
    
    /**
     * Convenience factory for not found errors.
     */
    public static ErrorResponse notFound(String message, String path) {
        return of("NOT_FOUND", message, 404, path);
    }
    
    /**
     * Convenience factory for validation errors.
     */
    public static ErrorResponse validationError(String message, String path) {
        return of("VALIDATION_ERROR", message, 400, path);
    }
}