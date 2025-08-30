package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ObservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public Page<ObservationDTO> getObservations(Pageable pageable) {
        var observations = observationRepository.findAll(pageable);
        return observations.map(ObservationDTO::from);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approveObservation(@PathVariable Long id, 
                                               @RequestParam(required = false) Integer expectedVersion) {
        var observationOpt = observationRepository.findById(id);
        
        return observationOpt.map(obs -> {
            // Pattern matching for optimistic locking check
            if (expectedVersion != null && !expectedVersion.equals(obs.getVersion())) {
                return ResponseEntity.status(409)
                    .body(String.format("Version conflict: expected %d, but was %d", 
                          expectedVersion, obs.getVersion()));
            }
            
            obs.setStatus(Observation.Status.APPROVED);
            var saved = observationService.saveWithScore(obs);
            return ResponseEntity.ok(ObservationDTO.from(saved));
        }).orElse(ResponseEntity.notFound().build());
    }
}
