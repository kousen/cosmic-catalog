package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.HealthInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for health check endpoints.
 * Delegates business logic to HealthService following single responsibility principle.
 */
@RestController
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    /**
     * Health check endpoint providing application status and metrics.
     * 
     * @return HealthInfo with version, counts, and last import time
     */
    @GetMapping("/health")
    public HealthInfo getHealth() {
        return healthService.getHealthInfo();
    }
}