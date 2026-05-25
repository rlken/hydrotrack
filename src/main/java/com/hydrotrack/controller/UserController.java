package com.hydrotrack.controller;

import com.hydrotrack.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("roles", userService.findAllRoles());
        return "admin/users";
    }

    @PostMapping("/users/new")
    public String createUser(@RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String fullName,
                              @RequestParam String password,
                              @RequestParam String role,
                              RedirectAttributes redirectAttributes) {
        try {
            userService.createUser(username, email, fullName, password, role);
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id,
                              @RequestParam String fullName,
                              @RequestParam String email,
                              @RequestParam String role,
                              @RequestParam(defaultValue = "false") Boolean isActive,
                              RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(id, fullName, email, role, isActive);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            var user = userService.findById(id);
            if (user != null && user.getUsername().equals(authentication.getName())) {
                redirectAttributes.addFlashAttribute("error", "You cannot delete the account you are currently using.");
                return "redirect:/admin/users";
            }
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
