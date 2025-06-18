package com.example.stock.service;

import com.example.stock.model.Security;
import com.example.stock.model.Stock;
import com.example.stock.util.MarketDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PortfolioScheduler {

    @Autowired
    private MarketDataProvider marketDataProvider;
    
    @Autowired
    private SecurityQuantityService securityQuantityService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private StockService stockService;

    @Scheduled(fixedDelay = 5000) // 5 seconds
    public void calculatePortfolioValue() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("PORTFOLIO VALUE CALCULATION - " + java.time.LocalDateTime.now());
        System.out.println("=".repeat(80));

        // Step 1: Get mocked stock prices and print them
        System.out.println("\n1. CURRENT STOCK PRICES:");
        System.out.println("-".repeat(40));
        Map<String, Double> stockPrices = getStockPrices();
        stockPrices.forEach((ticker, price) -> 
            System.out.printf("%-15s: $%.2f%n", ticker, price));

        // Step 2: Get all SECURITY data and calculate prices
        System.out.println("\n2. SECURITY PRICES:");
        System.out.println("-".repeat(40));
        List<Security> securities = securityService.findAll();
        
        BigDecimal totalPortfolioValue = BigDecimal.ZERO;
        
        for (Security security : securities) {
            String ticker = security.getTicker();
            String type = security.getType();
            Double strike = security.getStrike();
            
            // Get base stock price
            String stockId = security.getStock().getId();
            double stockPrice = stockPrices.getOrDefault(stockId, 0.0);
            
            // Calculate security price
            double securityPrice = stockPrice;
            if (strike != null) {
                // For options, multiply by strike price
                securityPrice = stockPrice * strike;
            }
            
            System.out.printf("%-25s: $%.2f (Type: %s, Strike: %s)%n", 
                ticker, securityPrice, type, strike != null ? strike.toString() : "N/A");
        }

        // Step 3: Get quantities and calculate total value
        System.out.println("\n3. POSITION VALUES:");
        System.out.println("-".repeat(80));
        System.out.printf("%-25s | %-10s | %-10s | %-15s%n", "TICKER", "PRICE", "QUANTITY", "VALUE");
        System.out.println("-".repeat(80));
        
        for (Security security : securities) {
            String ticker = security.getTicker();
            String type = security.getType();
            Double strike = security.getStrike();
            
            // Get quantity using service method
            Optional<Integer> quantityOpt = securityQuantityService.getQuantityByTicker(ticker);
            if (quantityOpt.isEmpty()) continue;
            Integer quantity = quantityOpt.get();
            
            // Get base stock price
            String stockId = security.getStock().getId();
            double stockPrice = stockPrices.getOrDefault(stockId, 0.0);
            
            // Calculate security price
            double securityPrice = stockPrice;
            if (strike != null) {
                securityPrice = stockPrice * strike;
            }
            
            // Calculate position value
            BigDecimal positionValue = BigDecimal.valueOf(securityPrice * quantity);
            totalPortfolioValue = totalPortfolioValue.add(positionValue);
            
            System.out.printf("%-25s | $%8.2f | %10d | $%13.2f%n", 
                ticker, securityPrice, quantity, positionValue);
        }
        System.out.println("-".repeat(80));

        // Step 4: Print total portfolio value
        System.out.println("\n4. TOTAL PORTFOLIO VALUE:");
        System.out.println("-".repeat(40));
        System.out.printf("TOTAL VALUE: $%15.2f%n", totalPortfolioValue);
        System.out.println("=".repeat(80));
    }

    private Map<String, Double> getStockPrices() {
        List<Stock> stocks = stockService.findAll();
        
        Map<String, Double> prices = new HashMap<>();
        for (Stock stock : stocks) {
            prices.put(stock.getId(), marketDataProvider.getCurrentPrice(stock.getId()));
        }
        return prices;
    }
} 