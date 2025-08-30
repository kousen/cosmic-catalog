package com.example.cosmiccatalog;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeDupServiceImpl implements DeDupService {

    private final ObservationRepository observationRepository;

    public DeDupServiceImpl(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @Override
    public Optional<Observation> findDuplicate(Observation newObservation) {
        List<Observation> candidates = observationRepository.findByTelescopeAndTargetNameAndFilters(
                newObservation.getTelescope(), newObservation.getTargetName(), newObservation.getFilters());

        return candidates.stream()
                .filter(candidate -> isDuplicate(candidate, newObservation))
                .findFirst();
    }

    private boolean isDuplicate(Observation existing, Observation fresh) {
        // Check for same telescope, target, and filter set is already done by the query

        // Check spatial proximity (within a few arcseconds)
        double raDiff = Math.abs(existing.getRa() - fresh.getRa()) * 3600; // degrees to arcseconds
        double decDiff = Math.abs(existing.getDec() - fresh.getDec()) * 3600; // degrees to arcseconds

        // BUG: This threshold is 10x too large, it should be ~5 arcseconds
        double threshold = 50.0;

        return raDiff < threshold && decDiff < threshold;
    }
}
