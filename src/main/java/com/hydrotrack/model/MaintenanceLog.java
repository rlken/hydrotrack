package com.hydrotrack.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_logs")
public class MaintenanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fountain_id", nullable = false)
    private Fountain fountain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private FilterSchedule schedule;

    @Column(name = "maintenance_type", nullable = false, length = 30)
    private String maintenanceType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "parts_replaced", length = 255)
    private String partsReplaced;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "performed_at", updatable = false)
    private LocalDateTime performedAt;

    public MaintenanceLog() {}

    @PrePersist
    protected void onCreate() { performedAt = LocalDateTime.now(); }

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    public Fountain getFountain() { return fountain; }
    public void setFountain(Fountain fountain) { this.fountain = fountain; }
    public User getPerformedBy() { return performedBy; }
    public void setPerformedBy(User performedBy) { this.performedBy = performedBy; }
    public FilterSchedule getSchedule() { return schedule; }
    public void setSchedule(FilterSchedule schedule) { this.schedule = schedule; }
    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPartsReplaced() { return partsReplaced; }
    public void setPartsReplaced(String partsReplaced) { this.partsReplaced = partsReplaced; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public LocalDateTime getPerformedAt() { return performedAt; }
    public void setPerformedAt(LocalDateTime performedAt) { this.performedAt = performedAt; }
}
