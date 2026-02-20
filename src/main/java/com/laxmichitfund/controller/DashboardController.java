package com.laxmichitfund.controller;

import com.laxmichitfund.entity.User;
import com.laxmichitfund.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    // Directs the root URL to the login page (index.html)
    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    // Handles the main trading terminal view
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        // 'Principal' contains the currently logged-in user's details
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByUsername(principal.getName());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // Add the user's virtual cash to the 'Model' so Thymeleaf can display it
                model.addAttribute("username", user.getUsername());
                model.addAttribute("virtualCash", String.format("%.2f", user.getVirtualCash()));
                model.addAttribute("userId", user.getId());
            }
        }
        return "dashboard"; // Renders dashboard.html
    }
}

