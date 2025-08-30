package com.example.cosmiccatalog.dto;

public record HealthInfo(
        String version,
        Counts counts,
        String lastImport
){
    public record Counts(int obs, int targets) {}
}

