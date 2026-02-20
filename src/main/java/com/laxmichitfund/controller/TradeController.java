package com.laxmichitfund.controller;

import com.laxmichitfund.dto.TradeRequest;
import com.laxmichitfund.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/execute")
    public ResponseEntity<String> executeOrder(@RequestBody TradeRequest request) {
        try {
            // Simulated 100ms delay to make it feel like a real exchange processing an order
            Thread.sleep(100); 
            
            String resultMessage = tradeService.executeTrade(request);
            return ResponseEntity.ok(resultMessage);
        } catch (Exception e) {
            // If anything goes wrong (like insufficient funds), send a 400 Bad Request
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

