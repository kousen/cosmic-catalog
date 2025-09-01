package com.example.cosmiccatalog.exception;

/**
 * Exception thrown when an optimistic locking version conflict occurs.
 */
public class VersionConflictException extends RuntimeException {
    private final Long entityId;
    private final Integer expectedVersion;
    private final Integer actualVersion;

    public VersionConflictException(Long entityId, Integer expectedVersion, Integer actualVersion) {
        super(String.format("Version conflict for entity %d: expected version %d, but was %d", 
                           entityId, expectedVersion, actualVersion));
        this.entityId = entityId;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }

    public Long getEntityId() {
        return entityId;
    }

    public Integer getExpectedVersion() {
        return expectedVersion;
    }

    public Integer getActualVersion() {
        return actualVersion;
    }
}