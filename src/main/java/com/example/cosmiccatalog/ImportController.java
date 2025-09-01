package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ImportSummary;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * REST endpoint for data import operations.
 */
@RestController
@RequestMapping("/api/import")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    /**
     * Imports sample JWST observations from the default data file.
     * 
     * @return import summary with statistics
     */
    @Operation(summary = "Import sample data", 
               description = "Loads sample JWST observations, deduplicates, scores, and saves them")
    @PostMapping("/sample")
    public ResponseEntity<ImportSummary> importSampleData() {
        try {
            var summary = importService.importSampleData();
            return ResponseEntity.ok(summary);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import sample data", e);
        }
    }
    
    /**
     * Imports realistic telescope observations based on actual JWST and Hubble programs.
     * 
     * @return import summary with statistics
     */
    @Operation(summary = "Import realistic telescope data", 
               description = "Loads 12 realistic observations from actual JWST and Hubble programs, with real target names and program IDs")
    @PostMapping("/realistic")
    public ResponseEntity<ImportSummary> importRealisticData() {
        try {
            var summary = importService.importRealisticData();
            return ResponseEntity.ok(summary);
        } catch (IOException e) {
            throw new RuntimeException("Failed to import realistic data", e);
        }
    }
}