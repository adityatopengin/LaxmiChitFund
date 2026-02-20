package com.laxmichitfund.service;

import com.laxmichitfund.dto.TradeRequest;
import com.laxmichitfund.entity.Portfolio;
import com.laxmichitfund.entity.User;
import com.laxmichitfund.repository.PortfolioRepository;
import com.laxmichitfund.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    // The simulated brokerage fee per trade (e.g., ₹20 or 0.05%)
    private static final double BROKERAGE_RATE = 0.0005;

    @Transactional
    public String executeTrade(TradeRequest request) throws Exception {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new Exception("User not found"));

        double totalStockValue = request.getQuantity() * request.getCurrentPrice();
        double brokerage = totalStockValue * BROKERAGE_RATE;

        if ("BUY".equalsIgnoreCase(request.getTradeType())) {
            double totalCost = totalStockValue + brokerage;

            // 1. Check if user has enough dummy cash
            if (user.getVirtualCash() < totalCost) {
                throw new Exception("Insufficient virtual funds!");
            }

            // 2. Deduct money
            user.setVirtualCash(user.getVirtualCash() - totalCost);
            userRepository.save(user);

            // 3. Add to portfolio
            Optional<Portfolio> existingStock = portfolioRepository.findByUserAndStockSymbol(user, request.getStockSymbol());
            
            if (existingStock.isPresent()) {
                // If they already own it, average out the price
                Portfolio p = existingStock.get();
                p.updateHolding(request.getQuantity(), request.getCurrentPrice());
                portfolioRepository.save(p);
            } else {
                // If it's a new stock, create a new record
                Portfolio newPortfolio = new Portfolio(null, user, request.getStockSymbol(), request.getQuantity(), request.getCurrentPrice());
                portfolioRepository.save(newPortfolio);
            }
            return "Successfully bought " + request.getQuantity() + " shares of " + request.getStockSymbol();

        } else if ("SELL".equalsIgnoreCase(request.getTradeType())) {
            
            // 1. Check if they own the stock and have enough quantity
            Portfolio p = portfolioRepository.findByUserAndStockSymbol(user, request.getStockSymbol())
                    .orElseThrow(() -> new Exception("You do not own this stock"));

            if (p.getQuantity() < request.getQuantity()) {
                throw new Exception("Insufficient shares to sell");
            }

            // 2. Update portfolio quantity
            p.setQuantity(p.getQuantity() - request.getQuantity());
            if (p.getQuantity() == 0) {
                portfolioRepository.delete(p); // Remove from DB if they sold everything
            } else {
                portfolioRepository.save(p);
            }

            // 3. Add money to user account (Total Value minus brokerage)
            double netEarnings = totalStockValue - brokerage;
            user.setVirtualCash(user.getVirtualCash() + netEarnings);
            userRepository.save(user);

            return "Successfully sold " + request.getQuantity() + " shares of " + request.getStockSymbol();
        }

        throw new Exception("Invalid trade type");
    }
}

