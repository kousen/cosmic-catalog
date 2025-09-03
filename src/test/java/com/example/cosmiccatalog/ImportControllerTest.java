package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ImportSummary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for ImportController.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ImportService importService;

    @Test
    void testImportSampleData() throws Exception {
        // Mock the import service
        var now = LocalDateTime.now();
        var summary = new ImportSummary(
            "data/jwst_sample.json",
            now.minusSeconds(5),
            now,
            100,
            5,
            95,
            ImportSummary.ImportStatus.SUCCEEDED,
            "Import successful"
        );
        when(importService.importSampleData()).thenReturn(summary);

        mockMvc.perform(post("/api/import/sample")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProcessed").value(100))
                .andExpect(jsonPath("$.imported").value(95))
                .andExpect(jsonPath("$.duplicatesFound").value(5));
    }

    @Test
    void testImportSampleDataHandlesIOException() throws Exception {
        // Mock IO exception
        when(importService.importSampleData())
                .thenThrow(new java.io.IOException("File not found"));

        mockMvc.perform(post("/api/import/sample")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}