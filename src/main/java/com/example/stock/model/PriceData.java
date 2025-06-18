package com.example.stock.model;

import java.time.LocalDateTime;

public class PriceData {
    private String ticker;
    private double price;
    private LocalDateTime timestamp;

    public PriceData(String ticker, double price, LocalDateTime timestamp) {
        this.ticker = ticker;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getTicker() {
        return ticker;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("PriceData{ticker='%s', price=%.2f, timestamp=%s}", 
            ticker, price, timestamp);
    }
} 