package com.example.cosmiccatalog;

import com.example.cosmiccatalog.exception.EntityNotFoundException;
import com.example.cosmiccatalog.exception.VersionConflictException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for observation approval with optimistic locking.
 */
@Service
public class ApprovalService {

    private final ObservationRepository observationRepository;
    private final ObservationService observationService;

    public ApprovalService(ObservationRepository observationRepository,
                          ObservationService observationService) {
        this.observationRepository = observationRepository;
        this.observationService = observationService;
    }

    /**
     * Approves an observation with optimistic locking support.
     * 
     * @param id observation ID
     * @param expectedVersion expected version for optimistic locking (optional)
     * @return approved observation
     * @throws EntityNotFoundException if observation not found
     * @throws VersionConflictException if version mismatch
     */
    @Transactional
    @CacheEvict(value = "featured", allEntries = true)
    public Observation approve(Long id, Integer expectedVersion) {
        var observation = observationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Observation", id));

        // Check optimistic locking
        if (expectedVersion != null && !expectedVersion.equals(observation.getVersion())) {
            throw new VersionConflictException(id, expectedVersion, observation.getVersion());
        }

        observation.setStatus(Observation.Status.APPROVED);
        return observationService.saveWithScore(observation);
    }
}