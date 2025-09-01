package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ObservationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST endpoint for featured observations.
 * Returns top-N approved observations sorted by score.
 */
@RestController
@Validated
public class FeaturedController {

    private final ObservationRepository observationRepository;

    public FeaturedController(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    /**
     * Returns top-N approved observations sorted by score desc.
     *
     * @param limit maximum number of results (1-100, default 10)
     * @return list of ObservationDTO
     */
    @Operation(summary = "Get featured observations", 
               description = "Returns top approved observations sorted by score")
    @GetMapping("/api/featured")
    @Cacheable(value = "featured", key = "#limit")
    public List<ObservationDTO> getFeatured(
            @Parameter(description = "Maximum number of observations to return (1-100)")
            @RequestParam(defaultValue = "10") 
            @Min(value = 1, message = "Limit must be at least 1")
            @Max(value = 100, message = "Limit must not exceed 100")
            int limit) {
        var pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "score"));
        return observationRepository.findByStatus(Observation.Status.APPROVED, pageable)
                .stream()
                .map(ObservationDTO::from)
                .toList();
    }
}