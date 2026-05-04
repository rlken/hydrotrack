package com.hydrotrack.service;

import com.hydrotrack.model.*;
import com.hydrotrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WaterQualityService {

    private final WaterQualityRepository waterQualityRepository;
    private final FountainRepository fountainRepository;
    private final UserRepository userRepository;

    public WaterQualityService(WaterQualityRepository waterQualityRepository, FountainRepository fountainRepository, UserRepository userRepository) {
        this.waterQualityRepository = waterQualityRepository;
        this.fountainRepository = fountainRepository;
        this.userRepository = userRepository;
    }


    public List<WaterQualityRecord> findAll() {
        return waterQualityRepository.findAllOrderByRecordedAtDesc();
    }

    public WaterQualityRecord findById(Long id) {
        return waterQualityRepository.findById(id).orElse(null);
    }

    public List<WaterQualityRecord> findByFountainId(Long fountainId) {
        return waterQualityRepository.findByFountainFountainIdOrderByRecordedAtDesc(fountainId);
    }

    public WaterQualityRecord findLatestByFountainId(Long fountainId) {
        return waterQualityRepository.findLatestByFountainId(fountainId);
    }

    public List<WaterQualityRecord> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return waterQualityRepository.findByDateRange(start, end);
    }

    public long countByRating(String rating) {
        return waterQualityRepository.countByRating(rating);
    }

    @Transactional
    public WaterQualityRecord recordQuality(Long fountainId, String username,
                                             BigDecimal phLevel, BigDecimal turbidity,
                                             BigDecimal tds, BigDecimal temperature,
                                             String cleanlinessRating, String notes) {
        Fountain fountain = fountainRepository.findById(fountainId)
                .orElseThrow(() -> new RuntimeException("Fountain not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate pH range (0-14)
        if (phLevel != null && (phLevel.compareTo(BigDecimal.ZERO) < 0 || phLevel.compareTo(new BigDecimal("14")) > 0)) {
            throw new RuntimeException("pH level must be between 0 and 14");
        }

        WaterQualityRecord record = new WaterQualityRecord();
        record.setFountain(fountain);
        record.setRecordedBy(user);
        record.setPhLevel(phLevel);
        record.setTurbidity(turbidity);
        record.setTds(tds);
        record.setTemperature(temperature);
        record.setCleanlinessRating(cleanlinessRating);
        record.setNotes(notes);

        // Auto-update fountain status based on rating
        if ("CRITICAL".equals(cleanlinessRating) || "POOR".equals(cleanlinessRating)) {
            fountain.setStatus("NEEDS_MAINTENANCE");
            fountainRepository.save(fountain);
        }

        return waterQualityRepository.save(record);
    }
}
