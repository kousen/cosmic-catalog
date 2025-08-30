package com.example.cosmiccatalog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TargetRepository extends JpaRepository<Target, Long> {
    Optional<Target> findByName(String name);
}
