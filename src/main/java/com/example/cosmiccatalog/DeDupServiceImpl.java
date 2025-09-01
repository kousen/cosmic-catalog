package com.example.cosmiccatalog;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeDupServiceImpl implements DeDupService {

    private final ObservationRepository observationRepository;

    public DeDupServiceImpl(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @Override
    public Optional<Observation> findDuplicate(Observation newObservation) {
        var candidates = observationRepository.findByTelescopeAndTargetNameAndFilters(
                newObservation.getTelescope(), newObservation.getTargetName(), newObservation.getFilters());

        return candidates.stream()
                .filter(candidate -> isDuplicate(candidate, newObservation))
                .findFirst();
    }

    private boolean isDuplicate(Observation existing, Observation fresh) {
        // Using pattern matching with record patterns would be ideal here with DTOs
        // For now, using var for type inference
        
        var raDiff = Math.abs(existing.getRa() - fresh.getRa()) * 3600; // degrees to arcseconds
        var decDiff = Math.abs(existing.getDec() - fresh.getDec()) * 3600; // degrees to arcseconds

        // Threshold of ~5 arcseconds for near-duplicate detection
        var threshold = 5.0;
        
        // Modern approach: could use pattern matching in future iterations
        return switch (checkProximity(raDiff, decDiff, threshold)) {
            case DUPLICATE -> true;
            case DISTINCT -> false;
        };
    }
    
    private enum ProximityResult { DUPLICATE, DISTINCT }
    
    private ProximityResult checkProximity(double raDiff, double decDiff, double threshold) {
        return (raDiff < threshold && decDiff < threshold) 
            ? ProximityResult.DUPLICATE 
            : ProximityResult.DISTINCT;
    }
}
