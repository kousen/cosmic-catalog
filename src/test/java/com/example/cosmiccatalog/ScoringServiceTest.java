package com.example.cosmiccatalog;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoringServiceTest {

    private final ScoringService scoringService = new ScoringServiceImpl();

    @Test
    void testDeterministicScoreCalculation() {
        Observation observation = new Observation();
        observation.setExposureSec(700);            // 30
        observation.setObsDate(LocalDateTime.now().minusDays(30)); // 20
        observation.setInstrument("WFC3");         // 25
        observation.setFilters("none");            // 0

        int score = scoringService.calculateScore(observation);
        assertEquals(75, score);
    }

    @Test
    void testFilterBoostApplied() {
        Observation observation = new Observation();
        observation.setExposureSec(0);              // 0
        observation.setObsDate(LocalDateTime.now().minusYears(10)); // 0
        observation.setInstrument("none");         // 0
        observation.setFilters("F200W");           // 15 via regex

        int score = scoringService.calculateScore(observation);
        assertEquals(15, score);
    }
}
