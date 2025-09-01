package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ErrorResponse;
import com.example.cosmiccatalog.dto.ObservationDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for managing observations.
 * Exposes listing and approval with optimistic locking via expectedVersion.
 */
@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationRepository observationRepository;
    private final ObservationService observationService;

    public ObservationController(ObservationRepository observationRepository,
                                 ObservationService observationService) {
        this.observationRepository = observationRepository;
        this.observationService = observationService;
    }

    /**
     * Returns a paginated list of observations as DTOs.
     *
     * @param pageable Spring Data pagination and sorting
     * @return page of ObservationDTO
     */
    @GetMapping
    public Page<ObservationDTO> getObservations(Pageable pageable) {
        var observations = observationRepository.findAll(pageable);
        return observations.map(ObservationDTO::from);
    }

    /**
     * Approves an observation.
     * If expectedVersion is provided and doesn't match the entity version, a 409 response is returned.
     *
     * @param id observation id
     * @param expectedVersion optional optimistic lock guard
     * @param request http request (for error path)
     * @return approved ObservationDTO or ErrorResponse
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveObservation(@PathVariable Long id, 
                                               @RequestParam(required = false) Integer expectedVersion,
                                               HttpServletRequest request) {
        var observationOpt = observationRepository.findById(id);
        
        return observationOpt.map(obs -> {
            // Pattern matching for optimistic locking check with structured error response
            if (expectedVersion != null && !expectedVersion.equals(obs.getVersion())) {
                var errorResponse = ErrorResponse.versionConflict(
                    String.format("Version conflict: expected %d, but was %d", 
                                expectedVersion, obs.getVersion()),
                    request.getRequestURI()
                );
                return ResponseEntity.status(409).body(errorResponse);
            }
            
            obs.setStatus(Observation.Status.APPROVED);
            var saved = observationService.saveWithScore(obs);
            return ResponseEntity.ok(ObservationDTO.from(saved));
        }).orElse(ResponseEntity.status(404)
            .body(ErrorResponse.notFound(
                "Observation not found with id: " + id,
                request.getRequestURI()
            ))
        );
    }
}
