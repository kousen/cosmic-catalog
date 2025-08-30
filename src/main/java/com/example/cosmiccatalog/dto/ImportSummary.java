package com.example.cosmiccatalog.dto;

import java.time.LocalDateTime;

/**
 * Modern record for import operation summary
 */
public record ImportSummary(
    String source,
    LocalDateTime startedAt,
    LocalDateTime completedAt,
    int totalProcessed,
    int duplicatesFound,
    int imported,
    ImportStatus status,
    String notes
) {
    public enum ImportStatus {
        SUCCEEDED, FAILED, PARTIAL
    }
    
    /**
     * Compact constructor with validation
     */
    public ImportSummary {
        if (totalProcessed < 0 || duplicatesFound < 0 || imported < 0) {
            throw new IllegalArgumentException("Counts cannot be negative");
        }
    }
}