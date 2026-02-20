package com.laxmichitfund.controller;

import com.laxmichitfund.entity.Portfolio;
import com.laxmichitfund.entity.User;
import com.laxmichitfund.repository.PortfolioRepository;
import com.laxmichitfund.repository.UserRepository;
import com.laxmichitfund.service.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class PortfolioController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private MarketDataService marketDataService; // To fetch live prices

    @GetMapping("/portfolio")
    public String showPortfolio(Model model, Principal principal) {
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByUsername(principal.getName());
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                List<Portfolio> holdings = portfolioRepository.findAllByUser(user);
                
                double totalInvested = 0.0;
                double totalCurrentValue = 0.0;

                // For a highly realistic app, you would fetch the actual live price for each holding.
                // For demonstration, we'll use a mocked live price logic or the API if connected.
                for (Portfolio p : holdings) {
                    double invested = p.getQuantity() * p.getAverageBuyPrice();
                    totalInvested += invested;
                    
                    // In a real scenario, use: marketDataService.getLivePrice(p.getStockSymbol(), "TOKEN")
                    // Here, we simulate a live price to avoid API rate limits during testing
                    double livePrice = p.getAverageBuyPrice() * (1 + (Math.random() * 0.1 - 0.05)); 
                    totalCurrentValue += (p.getQuantity() * livePrice);
                }

                double totalPL = totalCurrentValue - totalInvested;

                model.addAttribute("username", user.getUsername());
                model.addAttribute("virtualCash", String.format("%.2f", user.getVirtualCash()));
                model.addAttribute("holdings", holdings);
                model.addAttribute("totalInvested", String.format("%.2f", totalInvested));
                model.addAttribute("totalCurrentValue", String.format("%.2f", totalCurrentValue));
                model.addAttribute("totalPL", String.format("%.2f", totalPL));
            }
        }
        return "portfolio";
    }
}

