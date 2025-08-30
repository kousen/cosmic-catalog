package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ObservationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeaturedControllerIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Autowired
    ObservationRepository observationRepository;

    @Autowired
    ObservationService observationService;

    @BeforeEach
    void clean() {
        observationRepository.deleteAll();
    }

    @Test
    void featuredReturnsApprovedOnlySortedByScore() {
        // High score approved
        Observation o1 = new Observation();
        o1.setTelescope("JWST");
        o1.setTargetName("Carina");
        o1.setFilters("F200W"); // +15
        o1.setRa(10.0); o1.setDec(-59.0);
        o1.setObsDate(LocalDateTime.now().minusDays(10)); // +20
        o1.setInstrument("WFC3"); // +25
        o1.setExposureSec(700); // +30 => total ~90
        o1.setStatus(Observation.Status.APPROVED);
        observationService.saveWithScore(o1);

        // Lower score approved
        Observation o2 = new Observation();
        o2.setTelescope("JWST");
        o2.setTargetName("Carina");
        o2.setFilters("none"); // +0
        o2.setRa(10.0); o2.setDec(-59.0);
        o2.setObsDate(LocalDateTime.now().minusYears(6)); // +0
        o2.setInstrument("ACS"); // +10
        o2.setExposureSec(50); // +0 => total 10
        o2.setStatus(Observation.Status.APPROVED);
        observationService.saveWithScore(o2);

        // High score but not approved
        Observation o3 = new Observation();
        o3.setTelescope("JWST");
        o3.setTargetName("Carina");
        o3.setFilters("F200W");
        o3.setRa(10.0); o3.setDec(-59.0);
        o3.setObsDate(LocalDateTime.now().minusDays(5));
        o3.setInstrument("WFC3");
        o3.setExposureSec(700);
        o3.setStatus(Observation.Status.PENDING);
        observationService.saveWithScore(o3);

        String url = "http://localhost:" + port + "/api/featured?limit=5";
        ResponseEntity<List<ObservationDTO>> resp = rest.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(200, resp.getStatusCode().value());
        List<ObservationDTO> list = resp.getBody();
        assertNotNull(list);
        assertEquals(2, list.size());

        // Ensure sorted by score desc: first item should be o1
        assertEquals(o1.getId(), list.get(0).id());
        assertEquals(o2.getId(), list.get(1).id());
    }
}

