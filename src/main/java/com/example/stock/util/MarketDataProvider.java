package com.example.stock.util;

import com.example.stock.model.PriceData;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MarketDataProvider {
    private final Map<String, Double> currentPrices = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    // GBM parameters
    private static final double MU = 0.0; // drift
    private static final double SIGMA = 0.2; // volatility
    private static final double INITIAL_PRICE = 100.0;
    
    @PostConstruct
    public void init() {
        // Initialize prices for some stocks
        currentPrices.put("AAPL", 100.0);
        currentPrices.put("TELSA", 400.0);
        currentPrices.put("MSFT", INITIAL_PRICE);
        
        // Start price updates
        scheduleNextUpdate();
    }
    
    private void scheduleNextUpdate() {
        // Random delay between 0.5 and 2 seconds
        long delay = 500 + random.nextInt(1500);
        scheduler.schedule(this::updatePrices, delay, TimeUnit.MILLISECONDS);
    }
    
    private void updatePrices() {
        
        for (Map.Entry<String, Double> entry : currentPrices.entrySet()) {
            String ticker = entry.getKey();
            double currentPrice = entry.getValue();
            
            // Geometric Brownian Motion formula
            double dt = 1.0 / 365.0; // assuming daily updates
            double drift = (MU - 0.5 * SIGMA * SIGMA) * dt;
            double diffusion = SIGMA * Math.sqrt(dt) * random.nextGaussian();
            double newPrice = currentPrice * Math.exp(drift + diffusion);
            
            // Update price
            currentPrices.put(ticker, newPrice);
        }
        
        // Schedule next update
        scheduleNextUpdate();
    }
    
    public double getCurrentPrice(String ticker) {
        return currentPrices.getOrDefault(ticker, INITIAL_PRICE);
    }
    
    public void addTicker(String ticker) {
        currentPrices.putIfAbsent(ticker, INITIAL_PRICE);
    }
} 