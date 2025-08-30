package com.example.cosmiccatalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationRepository observationRepository;

    public ObservationController(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @GetMapping
    public Page<Observation> getObservations(Pageable pageable) {
        return observationRepository.findAll(pageable);
    }

    // The "/featured" endpoint will be added later or implemented simply
}
