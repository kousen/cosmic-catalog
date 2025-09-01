package com.example.cosmiccatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HealthControllerIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Autowired
    ObservationRepository observationRepository;

    @Autowired
    TargetRepository targetRepository;

    @Autowired
    ImportBatchRepository importBatchRepository;

    @BeforeEach
    void clean() {
        observationRepository.deleteAll();
        targetRepository.deleteAll();
        importBatchRepository.deleteAll();
    }

    @Test
    void healthReturnsVersionCountsAndLastImport() {
        // seed target + observations
        Target t = new Target();
        t.setName("Carina");
        t.setRa(10.0);
        t.setDec(-59.0);
        targetRepository.save(t);

        Observation o1 = new Observation();
        o1.setTelescope("JWST");
        o1.setTargetName("Carina");
        o1.setFilters("F200W");
        o1.setRa(10.0);
        o1.setDec(-59.0);
        o1.setObsDate(LocalDateTime.now().minusDays(1));
        o1.setInstrument("WFC3");
        o1.setExposureSec(700);
        observationRepository.save(o1);

        Observation o2 = new Observation();
        o2.setTelescope("JWST");
        o2.setTargetName("Carina");
        o2.setFilters("F444W");
        o2.setRa(10.0);
        o2.setDec(-59.0);
        o2.setObsDate(LocalDateTime.now().minusDays(5));
        o2.setInstrument("ACS");
        o2.setExposureSec(50);
        observationRepository.save(o2);

        // seed latest import batch with fixed timestamp for consistent testing
        ImportBatch ib = new ImportBatch();
        ib.setSource("jwst_sample.json");
        // Use fixed timestamp without nanoseconds for consistent formatting
        LocalDateTime completed = LocalDateTime.of(2024, 9, 1, 12, 0, 0);
        ib.setStartedAt(completed.minusHours(1));
        ib.setCompletedAt(completed);
        ib.setTotalRows(2);
        ib.setImportedCount(2);
        ib.setDuplicateCount(0);
        ib.setStatus(ImportBatch.Status.SUCCEEDED);
        importBatchRepository.save(ib);

        ResponseEntity<Map> resp = rest.getForEntity("http://localhost:" + port + "/health", Map.class);
        assertEquals(200, resp.getStatusCode().value());
        Map<String, Object> body = resp.getBody();
        assertNotNull(body);
        assertEquals("1.0.0", body.get("version"));

        Map<String, Object> counts = (Map<String, Object>) body.get("counts");
        assertNotNull(counts);
        assertEquals(2, ((Number) counts.get("obs")).intValue());
        assertEquals(1, ((Number) counts.get("targets")).intValue());

        String expectedLastImport = completed.format(DateTimeFormatter.ISO_DATE_TIME);
        assertEquals(expectedLastImport, body.get("lastImport"));
    }

    @Test
    void healthLastImportNullWhenNoImports() {
        ResponseEntity<Map> resp = rest.getForEntity("http://localhost:" + port + "/health", Map.class);
        assertEquals(200, resp.getStatusCode().value());
        Map<String, Object> body = resp.getBody();
        assertNotNull(body);
        assertNull(body.get("lastImport"));
    }
}

