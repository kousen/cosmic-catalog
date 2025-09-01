package com.example.cosmiccatalog;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "observations", 
       indexes = {
           @Index(name = "idx_status_score", columnList = "status, score DESC"),
           @Index(name = "idx_program_id", columnList = "programId"),
           @Index(name = "idx_target_name", columnList = "targetName"),
           @Index(name = "idx_obs_date", columnList = "obsDate DESC")
       })
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telescope;
    private String programId;
    private String targetName;
    private double ra;
    private double dec;
    private LocalDateTime obsDate;
    private String instrument;
    private String filters;
    private int exposureSec;
    private String imageUrl;
    private int score;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Version
    private int version;

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelescope() {
        return telescope;
    }

    public void setTelescope(String telescope) {
        this.telescope = telescope;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public double getRa() {
        return ra;
    }

    public void setRa(double ra) {
        this.ra = ra;
    }

    public double getDec() {
        return dec;
    }

    public void setDec(double dec) {
        this.dec = dec;
    }

    public LocalDateTime getObsDate() {
        return obsDate;
    }

    public void setObsDate(LocalDateTime obsDate) {
        this.obsDate = obsDate;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public int getExposureSec() {
        return exposureSec;
    }

    public void setExposureSec(int exposureSec) {
        this.exposureSec = exposureSec;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
