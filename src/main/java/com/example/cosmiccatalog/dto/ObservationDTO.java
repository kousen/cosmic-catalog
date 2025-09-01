package com.example.cosmiccatalog.dto;

import com.example.cosmiccatalog.Observation;

import java.time.LocalDateTime;

/**
 * Modern DTO using Java record for Observation data transfer
 */
public record ObservationDTO(
    Long id,
    String telescope,
    String programId,
    String targetName,
    double ra,
    double dec,
    LocalDateTime obsDate,
    String instrument,
    String filters,
    int exposureSec,
    String imageUrl,
    int score,
    Observation.Status status,
    boolean hasDontPanicBadge
) {
    
    /**
     * Factory method to create DTO from entity
     */
    public static ObservationDTO from(Observation entity) {
        return new ObservationDTO(
            entity.getId(),
            entity.getTelescope(),
            entity.getProgramId(),
            entity.getTargetName(),
            entity.getRa(),
            entity.getDec(),
            entity.getObsDate(),
            entity.getInstrument(),
            entity.getFilters(),
            entity.getExposureSec(),
            entity.getImageUrl(),
            entity.getScore(),
            entity.getStatus(),
            entity.getScore() == 42
        );
    }
}