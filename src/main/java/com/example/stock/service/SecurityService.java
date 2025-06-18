package com.example.stock.service;

import com.example.stock.model.Security;
import com.example.stock.model.Stock;
import com.example.stock.repository.SecurityRepository;
import com.example.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecurityService {
    
    @Autowired
    private SecurityRepository securityRepository;
    
    @Autowired
    private StockRepository stockRepository;

    @Transactional
    public Security saveSecurity(Security security) {
        return securityRepository.save(security);
    }

    public Optional<Security> getSecurity(String ticker) {
        return securityRepository.findById(ticker);
    }

    public List<Security> getAllSecurities() {
        return securityRepository.findAll();
    }

    public List<Security> findAll() {
        return securityRepository.findAll();
    }

    public List<Security> getSecuritiesByStock(String stockId) {
        return securityRepository.findByStockId(stockId);
    }

    public List<Security> getSecuritiesByType(String type) {
        return securityRepository.findByType(type);
    }

    public List<Security> getSecuritiesByStockAndType(String stockId, String type) {
        return securityRepository.findByStockIdAndType(stockId, type);
    }

    @Transactional
    public Security createSecurity(String ticker, String stockId, String type, String maturity, Double strike) {
        Optional<Stock> stockOpt = stockRepository.findById(stockId);
        if (stockOpt.isEmpty()) {
            throw new IllegalArgumentException("Stock with ID " + stockId + " not found");
        }

        Security security = new Security();
        security.setTicker(ticker);
        security.setStock(stockOpt.get());
        security.setType(type);
        security.setMaturity(maturity);
        security.setStrike(strike);

        return securityRepository.save(security);
    }

    @Transactional
    public void deleteSecurity(String ticker) {
        securityRepository.deleteById(ticker);
    }
} 