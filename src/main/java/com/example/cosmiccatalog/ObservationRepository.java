package com.example.cosmiccatalog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findByTelescopeAndTargetNameAndFilters(String telescope, String targetName, String filters);
}
