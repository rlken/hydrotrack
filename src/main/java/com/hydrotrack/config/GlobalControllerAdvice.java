package com.hydrotrack.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.function.Function;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("requestURI")
    public String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("floorLabel")
    public Function<Integer, String> getFloorLabel() {
        return floor -> {
            if (floor == null) {
                return "Unknown Floor";
            }

            String suffix = switch (floor) {
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };

            return floor + suffix + " Floor";
        };
    }
}
