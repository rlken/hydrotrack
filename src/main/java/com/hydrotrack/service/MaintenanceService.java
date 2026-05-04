package com.hydrotrack.service;

import com.hydrotrack.model.*;
import com.hydrotrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final FountainRepository fountainRepository;
    private final FilterScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public MaintenanceService(MaintenanceLogRepository maintenanceLogRepository, FountainRepository fountainRepository, FilterScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.maintenanceLogRepository = maintenanceLogRepository;
        this.fountainRepository = fountainRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }


    public List<MaintenanceLog> findAll() {
        return maintenanceLogRepository.findAllOrderByPerformedAtDesc();
    }

    public MaintenanceLog findById(Long id) {
        return maintenanceLogRepository.findById(id).orElse(null);
    }

    public List<MaintenanceLog> findByFountainId(Long fountainId) {
        return maintenanceLogRepository.findByFountainFountainIdOrderByPerformedAtDesc(fountainId);
    }

    /**
     * Complete a maintenance task — this is wrapped in a transaction to ensure:
     * 1. Maintenance log is created
     * 2. Filter schedule is updated (if applicable)
     * 3. Fountain status and last_filter_change are updated
     * All or nothing — ACID compliance.
     */
    @Transactional
    public MaintenanceLog completeMaintenance(Long fountainId, String username, Long scheduleId,
                                               String maintenanceType, String description,
                                               String partsReplaced, BigDecimal cost) {
        Fountain fountain = fountainRepository.findById(fountainId)
                .orElseThrow(() -> new RuntimeException("Fountain not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FilterSchedule schedule = null;
        if (scheduleId != null) {
            schedule = scheduleRepository.findById(scheduleId).orElse(null);
        }

        // 1. Create maintenance log
        MaintenanceLog log = new MaintenanceLog();
        log.setFountain(fountain);
        log.setPerformedBy(user);
        log.setSchedule(schedule);
        log.setMaintenanceType(maintenanceType);
        log.setDescription(description);
        log.setPartsReplaced(partsReplaced);
        log.setCost(cost);
        maintenanceLogRepository.save(log);

        // 2. Update filter schedule if this was a filter change
        if (schedule != null && "FILTER_CHANGE".equals(maintenanceType)) {
            schedule.setStatus("COMPLETED");
            // Create next schedule
            FilterSchedule nextSchedule = new FilterSchedule();
            nextSchedule.setFountain(fountain);
            nextSchedule.setFilterType(schedule.getFilterType());
            nextSchedule.setReplacementIntervalDays(schedule.getReplacementIntervalDays());
            nextSchedule.setNextReplacementDate(LocalDate.now().plusDays(schedule.getReplacementIntervalDays()));
            nextSchedule.setPriority("NORMAL");
            nextSchedule.setStatus("SCHEDULED");
            scheduleRepository.save(schedule);
            scheduleRepository.save(nextSchedule);

            // Update fountain's last filter change date
            fountain.setLastFilterChange(LocalDate.now());
        }

        // 3. Update fountain status to operational after maintenance
        fountain.setStatus("OPERATIONAL");
        fountainRepository.save(fountain);

        return log;
    }
}
