package com.example.cosmiccatalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies the static OpenAPI spec is served and contains the realistic import path.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OpenApiStaticResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void openApiYamlContainsRealisticImportPath() throws Exception {
        mockMvc.perform(get("/openapi.yaml"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/api/import/realistic")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("openapi: 3.0.3")));
    }
}

