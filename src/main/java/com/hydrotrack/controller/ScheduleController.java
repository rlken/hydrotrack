package com.hydrotrack.controller;

import com.hydrotrack.model.FilterSchedule;
import com.hydrotrack.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final FountainService fountainService;

    public ScheduleController(ScheduleService scheduleService, FountainService fountainService) {
        this.scheduleService = scheduleService;
        this.fountainService = fountainService;
    }


    @GetMapping
    public String listSchedules(Model model) {
        model.addAttribute("activeSchedules", scheduleService.findActiveSchedules());
        model.addAttribute("overdueSchedules", scheduleService.findOverdueSchedules());
        model.addAttribute("allSchedules", scheduleService.findAll());
        return "schedules/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String newScheduleForm(Model model) {
        model.addAttribute("fountains", fountainService.findAll());
        return "schedules/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String createSchedule(@RequestParam Long fountainId,
                                  @RequestParam String filterType,
                                  @RequestParam Integer replacementIntervalDays,
                                  @RequestParam String nextReplacementDate,
                                  @RequestParam(required = false) String priority,
                                  RedirectAttributes redirectAttributes) {
        try {
            scheduleService.createSchedule(fountainId, filterType, replacementIntervalDays,
                    LocalDate.parse(nextReplacementDate), priority);
            redirectAttributes.addFlashAttribute("success", "Schedule created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/schedules";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String editScheduleForm(@PathVariable Long id, Model model) {
        FilterSchedule schedule = scheduleService.findById(id);
        if (schedule == null) {
            return "redirect:/schedules";
        }
        model.addAttribute("schedule", schedule);
        model.addAttribute("fountains", fountainService.findAll());
        return "schedules/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String updateSchedule(@PathVariable Long id,
                                  @RequestParam String filterType,
                                  @RequestParam Integer replacementIntervalDays,
                                  @RequestParam String nextReplacementDate,
                                  @RequestParam(required = false) String priority,
                                  @RequestParam String status,
                                  RedirectAttributes redirectAttributes) {
        try {
            scheduleService.updateSchedule(id, filterType, replacementIntervalDays,
                    LocalDate.parse(nextReplacementDate), priority, status);
            redirectAttributes.addFlashAttribute("success", "Schedule updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/schedules";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSchedule(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            scheduleService.deleteSchedule(id);
            redirectAttributes.addFlashAttribute("success", "Schedule deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/schedules";
    }
}
