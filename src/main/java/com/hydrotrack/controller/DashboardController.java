package com.hydrotrack.controller;

import com.hydrotrack.model.*;
import com.hydrotrack.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final ReportService reportService;
    private final FountainService fountainService;
    private final WaterQualityService waterQualityService;
    private final ScheduleService scheduleService;

    public DashboardController(ReportService reportService, FountainService fountainService, WaterQualityService waterQualityService, ScheduleService scheduleService) {
        this.reportService = reportService;
        this.fountainService = fountainService;
        this.waterQualityService = waterQualityService;
        this.scheduleService = scheduleService;
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = reportService.getDashboardStats();
        model.addAllAttributes(stats);

        // Recent quality records (last 5)
        List<WaterQualityRecord> recentRecords = waterQualityService.findAll();
        model.addAttribute("recentRecords",
                recentRecords.size() > 5 ? recentRecords.subList(0, 5) : recentRecords);

        // Overdue filters
        List<FilterSchedule> overdueSchedules = scheduleService.findOverdueSchedules();
        model.addAttribute("overdueSchedules", overdueSchedules);

        // Upcoming schedules (next 7 days)
        List<FilterSchedule> upcomingSchedules = scheduleService.findUpcomingSchedules(7);
        model.addAttribute("upcomingSchedules", upcomingSchedules);

        // All floors
        model.addAttribute("floors", fountainService.findAllFloors());

        return "dashboard";
    }
}
