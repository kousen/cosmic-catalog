package com.example.cosmiccatalog;

import org.springframework.stereotype.Service;

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
}

