package com.example.stock.service;

import com.example.stock.model.Security;
import com.example.stock.model.SecurityQuantity;
import com.example.stock.repository.SecurityQuantityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityQuantityServiceTest {

    @Mock
    private SecurityQuantityRepository securityQuantityRepository;

    @InjectMocks
    private SecurityQuantityService securityQuantityService;

    private Security security;
    private SecurityQuantity securityQuantity1;
    private SecurityQuantity securityQuantity2;

    @BeforeEach
    void setUp() {
        security = new Security();
        security.setTicker("AAPL");
        security.setType("STOCK");

        securityQuantity1 = new SecurityQuantity();
        securityQuantity1.setId(1L);
        securityQuantity1.setSecurity(security);
        securityQuantity1.setQuantity(1000);
        securityQuantity1.setCreatedAt(LocalDateTime.now());

        securityQuantity2 = new SecurityQuantity();
        securityQuantity2.setId(2L);
        securityQuantity2.setSecurity(security);
        securityQuantity2.setQuantity(-20000);
        securityQuantity2.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSaveSecurityQuantity() {
        when(securityQuantityRepository.save(any(SecurityQuantity.class))).thenReturn(securityQuantity1);

        SecurityQuantity savedQuantity = securityQuantityService.saveSecurityQuantity(securityQuantity1);

        assertNotNull(savedQuantity);
        assertEquals(1L, savedQuantity.getId());
        assertEquals(1000, savedQuantity.getQuantity());
        verify(securityQuantityRepository).save(securityQuantity1);
    }

    @Test
    void testGetSecurityQuantity() {
        when(securityQuantityRepository.findById(1L)).thenReturn(Optional.of(securityQuantity1));

        Optional<SecurityQuantity> result = securityQuantityService.getSecurityQuantity(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(1000, result.get().getQuantity());
        verify(securityQuantityRepository).findById(1L);
    }

    @Test
    void testGetSecurityQuantityNotFound() {
        when(securityQuantityRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<SecurityQuantity> result = securityQuantityService.getSecurityQuantity(999L);

        assertFalse(result.isPresent());
        verify(securityQuantityRepository).findById(999L);
    }

    @Test
    void testGetAllSecurityQuantities() {
        List<SecurityQuantity> quantities = Arrays.asList(securityQuantity1, securityQuantity2);
        when(securityQuantityRepository.findAll()).thenReturn(quantities);

        List<SecurityQuantity> result = securityQuantityService.getAllSecurityQuantities();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(securityQuantityRepository).findAll();
    }

    @Test
    void testGetQuantityByTicker() {
        when(securityQuantityRepository.findQuantityByTicker("AAPL")).thenReturn(Optional.of(1000));

        Optional<Integer> result = securityQuantityService.getQuantityByTicker("AAPL");

        assertTrue(result.isPresent());
        assertEquals(1000, result.get());
        verify(securityQuantityRepository).findQuantityByTicker("AAPL");
    }

    @Test
    void testGetQuantityByTickerNotFound() {
        when(securityQuantityRepository.findQuantityByTicker("INVALID")).thenReturn(Optional.empty());

        Optional<Integer> result = securityQuantityService.getQuantityByTicker("INVALID");

        assertFalse(result.isPresent());
        verify(securityQuantityRepository).findQuantityByTicker("INVALID");
    }

    @Test
    void testFindByTicker() {
        when(securityQuantityRepository.findByTicker("AAPL")).thenReturn(Optional.of(securityQuantity1));

        Optional<SecurityQuantity> result = securityQuantityService.findByTicker("AAPL");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("AAPL", result.get().getSecurity().getTicker());
        verify(securityQuantityRepository).findByTicker("AAPL");
    }

    @Test
    void testFindByTickerNotFound() {
        when(securityQuantityRepository.findByTicker("INVALID")).thenReturn(Optional.empty());

        Optional<SecurityQuantity> result = securityQuantityService.findByTicker("INVALID");

        assertFalse(result.isPresent());
        verify(securityQuantityRepository).findByTicker("INVALID");
    }

    @Test
    void testDeleteSecurityQuantity() {
        doNothing().when(securityQuantityRepository).deleteById(1L);

        securityQuantityService.deleteSecurityQuantity(1L);

        verify(securityQuantityRepository).deleteById(1L);
    }
} 