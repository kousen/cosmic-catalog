package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.HealthInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Service responsible for gathering and computing application health metrics.
 * Extracts health check logic from the controller layer following SOLID principles.
 */
@Service
public class HealthService {

    private final ObservationRepository observationRepository;
    private final TargetRepository targetRepository;
    private final ImportBatchRepository importBatchRepository;
    private final String applicationVersion;

    public HealthService(ObservationRepository observationRepository,
                        TargetRepository targetRepository,
                        ImportBatchRepository importBatchRepository,
                        @Value("${app.version:0.0.1-SNAPSHOT}") String applicationVersion) {
        this.observationRepository = observationRepository;
        this.targetRepository = targetRepository;
        this.importBatchRepository = importBatchRepository;
        this.applicationVersion = applicationVersion;
    }

    /**
     * Gathers comprehensive health information about the application.
     * 
     * @return HealthInfo containing version, entity counts, and last import timestamp
     */
    public HealthInfo getHealthInfo() {
        var observationCount = observationRepository.count();
        var targetCount = targetRepository.count();
        var lastImportTime = getLastImportTime();
        
        return new HealthInfo(
            applicationVersion,
            new HealthInfo.Counts((int) observationCount, (int) targetCount),
            lastImportTime
        );
    }

    /**
     * Retrieves the timestamp of the most recent completed import.
     * 
     * @return ISO formatted timestamp string, or null if no imports exist
     */
    private String getLastImportTime() {
        return importBatchRepository.findTopByOrderByCompletedAtDesc()
                .map(batch -> batch.getCompletedAt() != null
                        ? batch.getCompletedAt().format(DateTimeFormatter.ISO_DATE_TIME)
                        : null)
                .orElse(null);
    }
}