package com.example.cosmiccatalog;

import com.example.cosmiccatalog.dto.ImportSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for importing observations from JSON files.
 */
@Service
public class ImportService {
    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);
    
    private final ObservationRepository observationRepository;
    private final ImportBatchRepository importBatchRepository;
    private final DeDupService deDupService;
    private final ObservationService observationService;
    private final ObjectMapper objectMapper;

    public ImportService(ObservationRepository observationRepository,
                        ImportBatchRepository importBatchRepository,
                        DeDupService deDupService,
                        ObservationService observationService,
                        ObjectMapper objectMapper) {
        this.observationRepository = observationRepository;
        this.importBatchRepository = importBatchRepository;
        this.deDupService = deDupService;
        this.observationService = observationService;
        this.objectMapper = objectMapper;
    }

    /**
     * Imports observations from the default sample file.
     * 
     * @return import summary with statistics
     * @throws IOException if file cannot be read
     */
    @Transactional
    public ImportSummary importSampleData() throws IOException {
        return importFromFile("data/jwst_sample.json");
    }
    
    /**
     * Imports realistic observations based on actual JWST/Hubble programs.
     * 
     * @return import summary with statistics
     * @throws IOException if file cannot be read
     */
    @Transactional
    public ImportSummary importRealisticData() throws IOException {
        return importFromFile("data/realistic_jwst.json");
    }

    /**
     * Imports observations from a specified file.
     * 
     * @param filename the file to import from
     * @return import summary with statistics
     * @throws IOException if file cannot be read
     */
    @Transactional
    public ImportSummary importFromFile(String filename) throws IOException {
        logger.info("Starting import from file: {}", filename);
        
        // Create import batch record
        var importBatch = new ImportBatch();
        importBatch.setSource(filename);
        importBatch.setStartedAt(LocalDateTime.now());
        
        // Read observations from file
        var resource = new ClassPathResource(filename);
        var observations = readObservationsFromFile(resource);
        importBatch.setTotalRows(observations.size());
        
        // Process observations
        var imported = new ArrayList<Observation>();
        var duplicates = 0;
        
        for (var observation : observations) {
            if (deDupService.findDuplicate(observation).isPresent()) {
                duplicates++;
                logger.debug("Skipping duplicate observation: {}", observation.getProgramId());
            } else {
                // Save with scoring
                var saved = observationService.saveWithScore(observation);
                imported.add(saved);
            }
        }
        
        // Update import batch statistics
        importBatch.setDuplicateCount(duplicates);
        importBatch.setImportedCount(imported.size());
        importBatch.setCompletedAt(LocalDateTime.now());
        importBatch.setStatus(ImportBatch.Status.SUCCEEDED);
        importBatchRepository.save(importBatch);
        
        logger.info("Import completed: {} records imported, {} duplicates skipped", 
                   imported.size(), duplicates);
        
        return new ImportSummary(
            filename,
            importBatch.getStartedAt(),
            importBatch.getCompletedAt(),
            observations.size(),
            duplicates,
            imported.size(),
            ImportSummary.ImportStatus.SUCCEEDED,
            String.format("Imported %d records, skipped %d duplicates", imported.size(), duplicates)
        );
    }

    private List<Observation> readObservationsFromFile(ClassPathResource resource) throws IOException {
        try (var inputStream = resource.getInputStream()) {
            var jsonNode = objectMapper.readTree(inputStream);
            var observations = new ArrayList<Observation>();
            
            if (jsonNode.isArray()) {
                for (var node : jsonNode) {
                    var observation = objectMapper.treeToValue(node, Observation.class);
                    observations.add(observation);
                }
            }
            
            return observations;
        }
    }
}