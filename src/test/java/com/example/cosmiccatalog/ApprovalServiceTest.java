package com.example.cosmiccatalog;

import com.example.cosmiccatalog.exception.EntityNotFoundException;
import com.example.cosmiccatalog.exception.VersionConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ApprovalService with optimistic locking.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ApprovalServiceTest {

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private ObservationRepository observationRepository;

    @Test
    void testApproveObservation() {
        // Create test observation
        var observation = new Observation();
        observation.setTelescope("JWST");
        observation.setProgramId("TEST-001");
        observation.setTargetName("Test Target");
        observation.setRa(10.0);
        observation.setDec(20.0);
        observation.setObsDate(java.time.LocalDateTime.now());
        observation.setInstrument("NIRCam");
        observation.setFilters("F090W");
        observation.setExposureSec(3600);
        observation.setStatus(Observation.Status.PENDING);
        observation = observationRepository.save(observation);

        // Approve observation
        var approved = approvalService.approve(observation.getId(), null);
        
        assertEquals(Observation.Status.APPROVED, approved.getStatus());
        assertTrue(approved.getScore() > 0);
    }

    @Test
    void testApproveWithCorrectVersion() {
        // Create test observation
        var observation = new Observation();
        observation.setTelescope("JWST");
        observation.setProgramId("TEST-002");
        observation.setTargetName("Test Target");
        observation.setRa(10.0);
        observation.setDec(20.0);
        observation.setObsDate(java.time.LocalDateTime.now());
        observation.setInstrument("NIRCam");
        observation.setFilters("F090W");
        observation.setExposureSec(3600);
        observation = observationRepository.save(observation);
        
        // Approve with correct version
        var approved = approvalService.approve(observation.getId(), observation.getVersion());
        
        assertEquals(Observation.Status.APPROVED, approved.getStatus());
    }

    @Test
    void testApproveWithWrongVersionThrows409() {
        // Create test observation
        var observation = new Observation();
        observation.setTelescope("JWST");
        observation.setProgramId("TEST-003");
        observation.setTargetName("Test Target");
        observation.setRa(10.0);
        observation.setDec(20.0);
        observation.setObsDate(java.time.LocalDateTime.now());
        var savedObservation = observationRepository.save(observation);
        
        // Try to approve with wrong version
        assertThrows(VersionConflictException.class, 
            () -> approvalService.approve(savedObservation.getId(), 999));
    }

    @Test
    void testApproveNonExistentObservationThrows404() {
        assertThrows(EntityNotFoundException.class, 
            () -> approvalService.approve(999L, null));
    }
}