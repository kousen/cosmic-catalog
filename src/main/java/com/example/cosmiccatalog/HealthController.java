package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.HealthInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@RestController
public class HealthController {

    private final ObservationRepository observationRepository;
    private final TargetRepository targetRepository;
    private final ImportBatchRepository importBatchRepository;

    private final String version;

    public HealthController(ObservationRepository observationRepository,
                            TargetRepository targetRepository,
                            ImportBatchRepository importBatchRepository,
                            @Value("${app.version:0.0.1-SNAPSHOT}") String version) {
        this.observationRepository = observationRepository;
        this.targetRepository = targetRepository;
        this.importBatchRepository = importBatchRepository;
        this.version = version;
    }

    @GetMapping("/health")
    public HealthInfo getHealth() {
        long obs = observationRepository.count();
        long targets = targetRepository.count();

        var latest = importBatchRepository.findTopByOrderByCompletedAtDesc()
                .map(ib -> ib.getCompletedAt() != null
                        ? ib.getCompletedAt().format(DateTimeFormatter.ISO_DATE_TIME)
                        : null)
                .orElse(null);

        return new HealthInfo(version, new HealthInfo.Counts((int) obs, (int) targets), latest);
    }
}

