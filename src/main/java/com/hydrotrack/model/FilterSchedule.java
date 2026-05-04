package com.hydrotrack.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "filter_schedules")
public class FilterSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fountain_id", nullable = false)
    private Fountain fountain;

    @Column(name = "filter_type", nullable = false, length = 50)
    private String filterType;

    @Column(name = "replacement_interval_days", nullable = false)
    private Integer replacementIntervalDays;

    @Column(name = "next_replacement_date", nullable = false)
    private LocalDate nextReplacementDate;

    @Column(nullable = false, length = 20)
    private String priority = "NORMAL";

    @Column(nullable = false, length = 20)
    private String status = "SCHEDULED";

    public FilterSchedule() {}

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public Fountain getFountain() { return fountain; }
    public void setFountain(Fountain fountain) { this.fountain = fountain; }
    public String getFilterType() { return filterType; }
    public void setFilterType(String filterType) { this.filterType = filterType; }
    public Integer getReplacementIntervalDays() { return replacementIntervalDays; }
    public void setReplacementIntervalDays(Integer replacementIntervalDays) { this.replacementIntervalDays = replacementIntervalDays; }
    public LocalDate getNextReplacementDate() { return nextReplacementDate; }
    public void setNextReplacementDate(LocalDate nextReplacementDate) { this.nextReplacementDate = nextReplacementDate; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
