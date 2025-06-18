package com.example.stock.service;

import com.example.stock.model.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    
    @Autowired
    private StockRepository stockRepository;

    @Transactional
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Optional<Stock> getStock(String id) {
        return stockRepository.findById(id);
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    @Transactional
    public void deleteStock(String id) {
        stockRepository.deleteById(id);
    }
} 