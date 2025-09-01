package com.example.cosmiccatalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for FeaturedController with validation.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FeaturedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObservationRepository observationRepository;

    @Test
    void testGetFeaturedWithDefaultLimit() throws Exception {
        // Create approved observations
        for (int i = 0; i < 15; i++) {
            var observation = new Observation();
            observation.setTelescope("JWST");
            observation.setProgramId("FEAT-" + i);
            observation.setTargetName("Featured " + i);
            observation.setRa(10.0 + i);
            observation.setDec(20.0 + i);
            observation.setStatus(Observation.Status.APPROVED);
            observation.setScore(100 - i);
            observationRepository.save(observation);
        }

        mockMvc.perform(get("/api/featured"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFeaturedWithValidLimit() throws Exception {
        mockMvc.perform(get("/api/featured")
                .param("limit", "5"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFeaturedWithLimitTooSmall() throws Exception {
        mockMvc.perform(get("/api/featured")
                .param("limit", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetFeaturedWithLimitTooLarge() throws Exception {
        mockMvc.perform(get("/api/featured")
                .param("limit", "101"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetFeaturedWithInvalidLimit() throws Exception {
        mockMvc.perform(get("/api/featured")
                .param("limit", "invalid"))
                .andExpect(status().isBadRequest());
    }
}