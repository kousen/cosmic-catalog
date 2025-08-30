package com.example.cosmiccatalog;

import java.util.Optional;

public interface DeDupService {
    Optional<Observation> findDuplicate(Observation observation);
}
