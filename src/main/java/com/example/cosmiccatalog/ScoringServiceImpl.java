package com.example.cosmiccatalog;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ScoringServiceImpl implements ScoringService {

    @Override
    public int calculateScore(Observation observation) {
        var exposureScore = calculateExposureScore(observation.getExposureSec());
        var recencyScore = calculateRecencyScore(observation.getObsDate());
        var instrumentScore = calculateInstrumentScore(observation.getInstrument());
        var filterScore = calculateFilterScore(observation.getFilters());
        
        var totalScore = exposureScore + recencyScore + instrumentScore + filterScore;
        
        // Easter egg: "Don't Panic" - properly implemented
        if (totalScore == 42) {
            // Score of 42 triggers special badge in UI
            return 42; // The Answer to Life, the Universe, and Everything
        }
        
        return Math.min(100, totalScore);
    }
    
    private int calculateExposureScore(int exposureSec) {
        if (exposureSec > 600) return 30;
        if (exposureSec > 300) return 15;
        return 0;
    }
    
    private int calculateRecencyScore(LocalDateTime obsDate) {
        var daysSince = Duration.between(obsDate, LocalDateTime.now()).toDays();
        
        if (daysSince < 365) return 20;
        if (daysSince < 1825) return 10;
        return 0;
    }
    
    private int calculateInstrumentScore(String instrument) {
        // Modern pattern matching with text blocks for documentation
        return switch (instrument) {
            case String i when i.contains("NIRCAM") -> 30;  // JWST NIRCam
            case String i when i.contains("WFC3") -> 25;    // HST Wide Field Camera 3
            case String i when i.contains("ACS") -> 10;     // HST Advanced Camera
            default -> 0;
        };
    }
    
    private int calculateFilterScore(String filters) {
        // Using modern regex pattern matching
        return filters.matches(".*(F[1-4]\\d{2}[WM]).*") ? 15 : 0;
    }
}
