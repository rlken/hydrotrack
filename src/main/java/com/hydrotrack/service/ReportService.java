package com.hydrotrack.service;

import com.hydrotrack.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final FountainRepository fountainRepository;
    private final WaterQualityRepository waterQualityRepository;
    private final FilterScheduleRepository scheduleRepository;
    private final MaintenanceLogRepository maintenanceLogRepository;

    public ReportService(FountainRepository fountainRepository, WaterQualityRepository waterQualityRepository, FilterScheduleRepository scheduleRepository, MaintenanceLogRepository maintenanceLogRepository) {
        this.fountainRepository = fountainRepository;
        this.waterQualityRepository = waterQualityRepository;
        this.scheduleRepository = scheduleRepository;
        this.maintenanceLogRepository = maintenanceLogRepository;
    }


    /**
     * Generate dashboard summary statistics.
     */
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Fountain counts
        stats.put("totalFountains", fountainRepository.count());
        stats.put("operationalCount", fountainRepository.countByStatus("OPERATIONAL"));
        stats.put("needsMaintenanceCount", fountainRepository.countByStatus("NEEDS_MAINTENANCE"));
        stats.put("outOfServiceCount", fountainRepository.countByStatus("OUT_OF_SERVICE"));
        stats.put("underRepairCount", fountainRepository.countByStatus("UNDER_REPAIR"));

        // Quality ratings
        stats.put("excellentCount", waterQualityRepository.countByRating("EXCELLENT"));
        stats.put("goodCount", waterQualityRepository.countByRating("GOOD"));
        stats.put("fairCount", waterQualityRepository.countByRating("FAIR"));
        stats.put("poorCount", waterQualityRepository.countByRating("POOR"));
        stats.put("criticalCount", waterQualityRepository.countByRating("CRITICAL"));

        // Quality records total
        stats.put("totalRecords", waterQualityRepository.count());

        // Filter schedule alerts
        stats.put("overdueFilters", scheduleRepository.countOverdue());
        stats.put("upcomingFilters", scheduleRepository.findUpcomingSchedules(
                LocalDate.now(), LocalDate.now().plusDays(7)).size());

        // Maintenance stats
        int currentMonth = LocalDate.now().getMonthValue();
        int currentYear = LocalDate.now().getYear();
        stats.put("maintenanceThisMonth", maintenanceLogRepository.countByMonth(currentMonth, currentYear));

        return stats;
    }
}
