package com.hydrotrack.controller;

import com.hydrotrack.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String createGuestAccount(@RequestParam String username,
                                     @RequestParam String email,
                                     @RequestParam String fullName,
                                     @RequestParam String password,
                                     @RequestParam String confirmPassword,
                                     RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/register";
        }

        try {
            userService.createGuestAccount(username, email, fullName, password);
            redirectAttributes.addFlashAttribute("success", "Account created. You can sign in now.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/forgot-password";
        }

        try {
            userService.resetPassword(username, email, password);
            redirectAttributes.addFlashAttribute("success", "Password updated. Sign in with your new password.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("errorMessage", "You do not have permission to access this page.");
        return "error/403";
    }
}
