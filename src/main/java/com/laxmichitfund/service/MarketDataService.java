package com.laxmichitfund.service;

import com.angelbroking.smartapi.SmartConnect;
import com.angelbroking.smartapi.models.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MarketDataService {

    @Value("${smartapi.key}")
    private String apiKey;

    @Value("${smartapi.client.id}")
    private String clientId;

    @Value("${smartapi.client.password}")
    private String clientPassword;

    private SmartConnect smartConnect;
    private String jwtToken;

    // This runs automatically when the Spring Boot server starts
    @PostConstruct
    public void initializeConnection() {
        try {
            smartConnect = new SmartConnect();
            smartConnect.setApiKey(apiKey);
            
            // Note: In a real production app, you would pass the dynamic TOTP here.
            // For a dummy game, you can generate a static TOTP token for your developer account.
            String currentTotp = "123456"; 
            
            User user = smartConnect.generateSession(clientId, clientPassword, currentTotp);
            this.jwtToken = user.getAccessToken();
            smartConnect.setAccessToken(jwtToken);
            
            System.out.println("✅ Successfully connected to Angel One SmartAPI!");
        } catch (Exception e) {
            System.err.println("❌ Failed to connect to Market API: " + e.getMessage());
        }
    }

    /**
     * Fetches the Last Traded Price (LTP) from the NSE.
     * @param tradingSymbol e.g., "RELIANCE-EQ"
     * @param symbolToken e.g., "2885" (Reliance's unique NSE token)
     * @return The live price as a double
     */
    public double getLivePrice(String tradingSymbol, String symbolToken) {
        try {
            JSONObject ltpData = smartConnect.getLTP("NSE", tradingSymbol, symbolToken);
            return ltpData.getJSONObject("data").getDouble("ltp");
        } catch (Exception e) {
            System.err.println("Error fetching price for " + tradingSymbol + ": " + e.getMessage());
            return 0.0; // Fallback in case of API failure
        }
    }
}

