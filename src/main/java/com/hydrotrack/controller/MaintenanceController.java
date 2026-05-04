package com.hydrotrack.controller;

import com.hydrotrack.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final FountainService fountainService;
    private final ScheduleService scheduleService;

    public MaintenanceController(MaintenanceService maintenanceService, FountainService fountainService, ScheduleService scheduleService) {
        this.maintenanceService = maintenanceService;
        this.fountainService = fountainService;
        this.scheduleService = scheduleService;
    }


    @GetMapping
    public String listLogs(Model model) {
        model.addAttribute("logs", maintenanceService.findAll());
        return "maintenance/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String newMaintenanceForm(Model model) {
        model.addAttribute("fountains", fountainService.findAll());
        model.addAttribute("schedules", scheduleService.findActiveSchedules());
        return "maintenance/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String createMaintenance(@RequestParam Long fountainId,
                                     @RequestParam(required = false) Long scheduleId,
                                     @RequestParam String maintenanceType,
                                     @RequestParam(required = false) String description,
                                     @RequestParam(required = false) String partsReplaced,
                                     @RequestParam(required = false) BigDecimal cost,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.completeMaintenance(fountainId, authentication.getName(),
                    scheduleId, maintenanceType, description, partsReplaced, cost);
            redirectAttributes.addFlashAttribute("success", "Maintenance logged successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/maintenance";
    }
}
