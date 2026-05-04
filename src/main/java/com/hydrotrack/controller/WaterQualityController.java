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
@RequestMapping("/quality")
public class WaterQualityController {

    private final WaterQualityService waterQualityService;
    private final FountainService fountainService;

    public WaterQualityController(WaterQualityService waterQualityService, FountainService fountainService) {
        this.waterQualityService = waterQualityService;
        this.fountainService = fountainService;
    }


    @GetMapping
    public String listRecords(Model model) {
        model.addAttribute("records", waterQualityService.findAll());
        return "quality/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSPECTOR')")
    public String newRecordForm(Model model) {
        model.addAttribute("fountains", fountainService.findAll());
        return "quality/form";
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSPECTOR')")
    public String createRecord(@RequestParam Long fountainId,
                                @RequestParam(required = false) BigDecimal phLevel,
                                @RequestParam(required = false) BigDecimal turbidity,
                                @RequestParam(required = false) BigDecimal tds,
                                @RequestParam(required = false) BigDecimal temperature,
                                @RequestParam String cleanlinessRating,
                                @RequestParam(required = false) String notes,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            waterQualityService.recordQuality(fountainId, authentication.getName(),
                    phLevel, turbidity, tds, temperature, cleanlinessRating, notes);
            redirectAttributes.addFlashAttribute("success", "Quality record saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/quality";
    }
}
