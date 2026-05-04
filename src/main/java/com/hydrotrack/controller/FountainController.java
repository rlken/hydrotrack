package com.hydrotrack.controller;

import com.hydrotrack.model.Fountain;
import com.hydrotrack.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/fountains")
public class FountainController {

    private final FountainService fountainService;
    private final WaterQualityService waterQualityService;
    private final ScheduleService scheduleService;
    private final MaintenanceService maintenanceService;

    public FountainController(FountainService fountainService, WaterQualityService waterQualityService, ScheduleService scheduleService, MaintenanceService maintenanceService) {
        this.fountainService = fountainService;
        this.waterQualityService = waterQualityService;
        this.scheduleService = scheduleService;
        this.maintenanceService = maintenanceService;
    }


    @GetMapping
    public String listFountains(@RequestParam(required = false) Integer floor,
                                 @RequestParam(required = false) String status,
                                 Model model) {
        List<Fountain> fountains;

        if (floor != null) {
            fountains = fountainService.findByFloor(floor);
        } else if (status != null && !status.isEmpty()) {
            fountains = fountainService.findByStatus(status);
        } else {
            fountains = fountainService.findAll();
        }

        model.addAttribute("fountains", fountains);
        model.addAttribute("floors", fountainService.findAllFloors());
        model.addAttribute("selectedFloor", floor);
        model.addAttribute("selectedStatus", status);
        return "fountains/list";
    }

    @GetMapping("/{id}")
    public String fountainDetail(@PathVariable Long id, Model model) {
        Fountain fountain = fountainService.findById(id);
        if (fountain == null) {
            return "redirect:/fountains";
        }

        model.addAttribute("fountain", fountain);
        model.addAttribute("qualityRecords", waterQualityService.findByFountainId(id));
        model.addAttribute("latestQuality", waterQualityService.findLatestByFountainId(id));
        model.addAttribute("schedules", scheduleService.findByFountainId(id));
        model.addAttribute("maintenanceLogs", maintenanceService.findByFountainId(id));
        return "fountains/detail";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String newFountainForm(Model model) {
        model.addAttribute("floors", fountainService.findAllFloors());
        return "fountains/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String createFountain(@RequestParam String fountainCode,
                                  @RequestParam String fountainType,
                                  @RequestParam Integer floorNumber,
                                  @RequestParam(required = false) String locationDescription,
                                  @RequestParam(required = false) String installationDate,
                                  RedirectAttributes redirectAttributes) {
        try {
            LocalDate instDate = (installationDate != null && !installationDate.isEmpty())
                    ? LocalDate.parse(installationDate) : null;
            fountainService.createFountain(fountainCode, fountainType, floorNumber,
                    locationDescription, instDate);
            redirectAttributes.addFlashAttribute("success", "Fountain created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/fountains";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String editFountainForm(@PathVariable Long id, Model model) {
        Fountain fountain = fountainService.findById(id);
        if (fountain == null) {
            return "redirect:/fountains";
        }
        model.addAttribute("fountain", fountain);
        return "fountains/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public String updateFountain(@PathVariable Long id,
                                  @RequestParam String fountainType,
                                  @RequestParam Integer floorNumber,
                                  @RequestParam(required = false) String locationDescription,
                                  @RequestParam String status,
                                  RedirectAttributes redirectAttributes) {
        try {
            fountainService.updateFountain(id, fountainType, floorNumber, locationDescription, status);
            redirectAttributes.addFlashAttribute("success", "Fountain updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/fountains/" + id;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteFountain(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            fountainService.deleteFountain(id);
            redirectAttributes.addFlashAttribute("success", "Fountain deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/fountains";
    }
}
