package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ObservationDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeaturedController {

    private final ObservationRepository observationRepository;

    public FeaturedController(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @GetMapping("/api/featured")
    public List<ObservationDTO> getFeatured(@RequestParam(defaultValue = "10") int limit) {
        var pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "score"));
        return observationRepository.findByStatus(Observation.Status.APPROVED, pageable)
                .stream()
                .map(ObservationDTO::from)
                .toList();
    }
}

