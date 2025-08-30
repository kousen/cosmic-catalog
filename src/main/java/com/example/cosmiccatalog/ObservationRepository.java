package com.example.cosmiccatalog;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findByTelescopeAndTargetNameAndFilters(String telescope, String targetName, String filters);
    List<Observation> findByStatus(Observation.Status status, Pageable pageable);
}
