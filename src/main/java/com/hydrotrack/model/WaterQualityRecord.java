package com.hydrotrack.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "water_quality_records")
public class WaterQualityRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fountain_id", nullable = false)
    private Fountain fountain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by", nullable = false)
    private User recordedBy;

    @Column(name = "ph_level", precision = 4, scale = 2)
    private BigDecimal phLevel;

    @Column(precision = 6, scale = 2)
    private BigDecimal turbidity;

    @Column(precision = 7, scale = 2)
    private BigDecimal tds;

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "cleanliness_rating", nullable = false, length = 20)
    private String cleanlinessRating;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;

    public WaterQualityRecord() {}

    @PrePersist
    protected void onCreate() { recordedAt = LocalDateTime.now(); }

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public Fountain getFountain() { return fountain; }
    public void setFountain(Fountain fountain) { this.fountain = fountain; }
    public User getRecordedBy() { return recordedBy; }
    public void setRecordedBy(User recordedBy) { this.recordedBy = recordedBy; }
    public BigDecimal getPhLevel() { return phLevel; }
    public void setPhLevel(BigDecimal phLevel) { this.phLevel = phLevel; }
    public BigDecimal getTurbidity() { return turbidity; }
    public void setTurbidity(BigDecimal turbidity) { this.turbidity = turbidity; }
    public BigDecimal getTds() { return tds; }
    public void setTds(BigDecimal tds) { this.tds = tds; }
    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }
    public String getCleanlinessRating() { return cleanlinessRating; }
    public void setCleanlinessRating(String cleanlinessRating) { this.cleanlinessRating = cleanlinessRating; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}
