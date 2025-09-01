package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ObservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * REST endpoints for managing observations.
 * Exposes listing and approval with optimistic locking via expectedVersion.
 */
@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationRepository observationRepository;
    private final ApprovalService approvalService;

    public ObservationController(ObservationRepository observationRepository,
                                 ApprovalService approvalService) {
        this.observationRepository = observationRepository;
        this.approvalService = approvalService;
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
    public ObservationDTO approveObservation(@PathVariable Long id, 
                                            @RequestParam(required = false) Integer expectedVersion) {
        var approved = approvalService.approve(id, expectedVersion);
        return ObservationDTO.from(approved);
    }
}
