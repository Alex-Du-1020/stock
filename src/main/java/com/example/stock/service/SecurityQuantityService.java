package com.example.stock.service;

import com.example.stock.model.SecurityQuantity;
import com.example.stock.repository.SecurityQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityQuantityService {
    
    @Autowired
    private SecurityQuantityRepository securityQuantityRepository;

    @Transactional
    public SecurityQuantity saveSecurityQuantity(SecurityQuantity securityQuantity) {
        return securityQuantityRepository.save(securityQuantity);
    }

    public Optional<SecurityQuantity> getSecurityQuantity(Long id) {
        return securityQuantityRepository.findById(id);
    }

    public List<SecurityQuantity> getAllSecurityQuantities() {
        return securityQuantityRepository.findAll();
    }

    public Optional<Integer> getQuantityByTicker(String ticker) {
        return securityQuantityRepository.findQuantityByTicker(ticker);
    }

    public Optional<SecurityQuantity> findByTicker(String ticker) {
        return securityQuantityRepository.findByTicker(ticker);
    }

    @Transactional
    public void deleteSecurityQuantity(Long id) {
        securityQuantityRepository.deleteById(id);
    }
} 