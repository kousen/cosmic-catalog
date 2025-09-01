package com.example.cosmiccatalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ObservationController with version conflict handling.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ObservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObservationRepository observationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetObservations() throws Exception {
        // Create test observations
        for (int i = 0; i < 5; i++) {
            var observation = new Observation();
            observation.setTelescope("JWST");
            observation.setProgramId("OBS-" + i);
            observation.setTargetName("Target " + i);
            observation.setRa(10.0 + i);
            observation.setDec(20.0 + i);
            observation.setObsDate(LocalDateTime.now());
            observation.setInstrument("NIRCam");
            observation.setFilters("F090W");
            observation.setExposureSec(3600);
            observationRepository.save(observation);
        }

        mockMvc.perform(get("/api/observations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(5));
    }

    @Test
    void testApproveObservation() throws Exception {
        // Create test observation
        var observation = new Observation();
        observation.setTelescope("JWST");
        observation.setProgramId("APPROVE-001");
        observation.setTargetName("Test Target");
        observation.setRa(10.0);
        observation.setDec(20.0);
        observation.setObsDate(LocalDateTime.now());
        observation.setInstrument("NIRCam");
        observation.setFilters("F090W");
        observation.setExposureSec(3600);
        observation = observationRepository.save(observation);

        mockMvc.perform(post("/api/observations/{id}/approve", observation.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testApproveWithCorrectVersion() throws Exception {
        // Create test observation
        var observation = new Observation();
        observation.setTelescope("JWST");
        observation.setProgramId("VERSION-001");
        observation.setTargetName("Test Target");
        observation.setRa(10.0);
        observation.setDec(20.0);
        observation.setObsDate(LocalDateTime.now());
        observation.setInstrument("NIRCam");
        observation.setFilters("F090W");
        observation.setExposureSec(3600);
        observation = observationRepository.save(observation);

        mockMvc.perform(post("/api/observations/{id}/approve", observation.getId())
                .param("expectedVersion", String.valueOf(observation.getVersion())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testApproveWithWrongVersionReturns409() throws Exception {
        // Create test observation
        var observation = new Observation();
        observation.setTelescope("JWST");
        observation.setProgramId("CONFLICT-001");
        observation.setTargetName("Test Target");
        observation.setRa(10.0);
        observation.setDec(20.0);
        observation.setObsDate(LocalDateTime.now());
        observation.setInstrument("NIRCam");
        observation.setFilters("F090W");
        observation.setExposureSec(3600);
        observation = observationRepository.save(observation);

        mockMvc.perform(post("/api/observations/{id}/approve", observation.getId())
                .param("expectedVersion", "999"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("VERSION_CONFLICT"));
    }

    @Test
    void testApproveNonExistentReturns404() throws Exception {
        mockMvc.perform(post("/api/observations/{id}/approve", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }
}