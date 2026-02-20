package com.laxmichitfund.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequest {
    
    private Long userId;          // Who is making the trade
    private String stockSymbol;   // e.g., "RELIANCE" or "TCS"
    private Integer quantity;     // How many shares they want to buy/sell
    private String tradeType;     // Must be "BUY" or "SELL"
    private Double currentPrice;  // The live market price at the moment of the click
    
}

