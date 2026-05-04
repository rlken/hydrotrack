package com.hydrotrack.service;

import com.hydrotrack.model.*;
import com.hydrotrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class FountainService {

    private final FountainRepository fountainRepository;
    private final BuildingRepository buildingRepository;

    public FountainService(FountainRepository fountainRepository, BuildingRepository buildingRepository) {
        this.fountainRepository = fountainRepository;
        this.buildingRepository = buildingRepository;
    }


    public List<Fountain> findAll() {
        return fountainRepository.findAll();
    }

    public Fountain findById(Long id) {
        return fountainRepository.findById(id).orElse(null);
    }

    public Fountain findByCode(String code) {
        return fountainRepository.findByFountainCode(code).orElse(null);
    }

    public List<Fountain> findByFloor(Integer floor) {
        return fountainRepository.findByFloorNumber(floor);
    }

    public List<Fountain> findByStatus(String status) {
        return fountainRepository.findByStatus(status);
    }

    public List<Integer> findAllFloors() {
        return fountainRepository.findAllFloors();
    }

    public long countByStatus(String status) {
        return fountainRepository.countByStatus(status);
    }

    @Transactional
    public Fountain createFountain(String fountainCode, String fountainType, Integer floorNumber,
                                    String locationDescription, LocalDate installationDate) {
        // Default to first building (NU Manila Main Building)
        Building building = buildingRepository.findAll().get(0);

        Fountain fountain = new Fountain();
        fountain.setBuilding(building);
        fountain.setFountainCode(fountainCode);
        fountain.setFountainType(fountainType);
        fountain.setFloorNumber(floorNumber);
        fountain.setLocationDescription(locationDescription);
        fountain.setInstallationDate(installationDate);
        fountain.setStatus("OPERATIONAL");

        return fountainRepository.save(fountain);
    }

    @Transactional
    public Fountain updateFountain(Long id, String fountainType, Integer floorNumber,
                                    String locationDescription, String status) {
        Fountain fountain = fountainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fountain not found"));

        fountain.setFountainType(fountainType);
        fountain.setFloorNumber(floorNumber);
        fountain.setLocationDescription(locationDescription);
        fountain.setStatus(status);

        return fountainRepository.save(fountain);
    }

    @Transactional
    public void deleteFountain(Long id) {
        fountainRepository.deleteById(id);
    }
}
