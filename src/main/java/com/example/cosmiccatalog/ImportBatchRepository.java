package com.example.cosmiccatalog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImportBatchRepository extends JpaRepository<ImportBatch, Long> {
    java.util.Optional<ImportBatch> findTopByOrderByCompletedAtDesc();
}
