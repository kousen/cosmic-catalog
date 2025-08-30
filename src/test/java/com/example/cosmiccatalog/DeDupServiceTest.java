package com.example.cosmiccatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class DeDupServiceTest {

    @Mock
    private ObservationRepository observationRepository;

    private DeDupService deDupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deDupService = new DeDupServiceImpl(observationRepository);
    }

    @Test
    void testFindDuplicate_shouldFindNearDuplicate() {
        Observation existing = new Observation();
        existing.setTelescope("JWST");
        existing.setTargetName("Carina");
        existing.setFilters("F200W");
        existing.setRa(10.684708);
        existing.setDec(-59.70444);
        existing.setObsDate(LocalDateTime.now());

        Observation fresh = new Observation();
        fresh.setTelescope("JWST");
        fresh.setTargetName("Carina");
        fresh.setFilters("F200W");
        // RA/Dec are slightly different, but within 3 arcseconds
        fresh.setRa(10.684708 + 2.0 / 3600.0);
        fresh.setDec(-59.70444 + 1.0 / 3600.0);
        fresh.setObsDate(LocalDateTime.now().plusHours(1));

        when(observationRepository.findByTelescopeAndTargetNameAndFilters("JWST", "Carina", "F200W"))
                .thenReturn(Collections.singletonList(existing));

        Optional<Observation> result = deDupService.findDuplicate(fresh);

        // This assertion will fail because the threshold in DeDupServiceImpl is too high
        assertTrue(result.isPresent(), "A near-duplicate should have been found");
    }
}
