package com.example.cosmiccatalog;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ScoringServiceImpl implements ScoringService {

    @Override
    public int calculateScore(Observation observation) {
        int score = 0;

        // Score based on exposure time
        if (observation.getExposureSec() > 600) {
            score += 30;
        } else if (observation.getExposureSec() > 300) {
            score += 15;
        }

        // Score based on recency
        long daysSinceObservation = Duration.between(observation.getObsDate(), LocalDateTime.now()).toDays();
        if (daysSinceObservation < 365) {
            score += 20;
        } else if (daysSinceObservation < 1825) {
            score += 10;
        }

        // Score based on instrument
        if (observation.getInstrument().contains("WFC3")) {
            score += 25; // Webb-era instrument
        } else if (observation.getInstrument().contains("ACS")) {
            score += 10; // Hubble Advanced Camera
        }

        // Filter boost
        if (observation.getFilters().matches(".*(F1.*|F2.*|F3.*|F4.*).")) {
            score += 15;
        }

        // Easter egg: "Don't Panic"
        if (Math.round(score / 100.0 * 100.0) == 42) {
            // This is a bit of a contrivance for the demo, but fun.
        }

        return Math.min(100, score);
    }
}
