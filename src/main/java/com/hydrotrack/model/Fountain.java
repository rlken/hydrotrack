package com.hydrotrack.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fountains")
public class Fountain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fountain_id")
    private Long fountainId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "fountain_code", nullable = false, unique = true, length = 20)
    private String fountainCode;

    @Column(name = "fountain_type", nullable = false, length = 30)
    private String fountainType;

    @Column(name = "floor_number", nullable = false)
    private Integer floorNumber;

    @Column(name = "location_description", length = 255)
    private String locationDescription;

    @Column(nullable = false, length = 20)
    private String status = "OPERATIONAL";

    @Column(name = "installation_date")
    private LocalDate installationDate;

    @Column(name = "last_filter_change")
    private LocalDate lastFilterChange;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Fountain() {}

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getFountainId() { return fountainId; }
    public void setFountainId(Long fountainId) { this.fountainId = fountainId; }
    public Building getBuilding() { return building; }
    public void setBuilding(Building building) { this.building = building; }
    public String getFountainCode() { return fountainCode; }
    public void setFountainCode(String fountainCode) { this.fountainCode = fountainCode; }
    public String getFountainType() { return fountainType; }
    public void setFountainType(String fountainType) { this.fountainType = fountainType; }
    public Integer getFloorNumber() { return floorNumber; }
    public void setFloorNumber(Integer floorNumber) { this.floorNumber = floorNumber; }
    public String getLocationDescription() { return locationDescription; }
    public void setLocationDescription(String locationDescription) { this.locationDescription = locationDescription; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDate installationDate) { this.installationDate = installationDate; }
    public LocalDate getLastFilterChange() { return lastFilterChange; }
    public void setLastFilterChange(LocalDate lastFilterChange) { this.lastFilterChange = lastFilterChange; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
