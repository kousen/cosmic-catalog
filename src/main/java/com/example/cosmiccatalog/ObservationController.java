package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ObservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationRepository observationRepository;
    // Virtual threads for async operations (Java 21+)
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public ObservationController(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @GetMapping
    public Page<ObservationDTO> getObservations(Pageable pageable) {
        var observations = observationRepository.findAll(pageable);
        return observations.map(ObservationDTO::from);
    }

    @GetMapping("/featured")
    public CompletableFuture<List<ObservationDTO>> getFeaturedObservations(
            @RequestParam(defaultValue = "10") int limit) {
        
        return CompletableFuture.supplyAsync(() -> {
            var pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "score"));
            var featuredObs = observationRepository.findByStatus(Observation.Status.APPROVED, pageable);
            
            return featuredObs.stream()
                    .map(ObservationDTO::from)
                    .toList(); // Using Java 16+ toList()
        }, executor);
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
            var saved = observationRepository.save(obs);
            return ResponseEntity.ok(ObservationDTO.from(saved));
        }).orElse(ResponseEntity.notFound().build());
    }
}
