package com.example.cosmiccatalog;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObservationService {

    private final ObservationRepository observationRepository;
    private final ScoringService scoringService;

    public ObservationService(ObservationRepository observationRepository, ScoringService scoringService) {
        this.observationRepository = observationRepository;
        this.scoringService = scoringService;
    }

    public Observation saveWithScore(Observation observation) {
        int score = scoringService.calculateScore(observation);
        observation.setScore(score);
        return observationRepository.save(observation);
    }
    
    /**
     * Gets the top N observations by score, with caching.
     * 
     * @param limit maximum number of observations to return
     * @return list of featured observations sorted by score descending
     */
    @Cacheable(value = "featured", key = "#limit")
    public List<Observation> getFeaturedObservations(int limit) {
        var pageable = PageRequest.of(0, limit, Sort.by("score").descending());
        return observationRepository.findAll(pageable).getContent();
    }
}

