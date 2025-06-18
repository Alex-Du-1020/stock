package com.example.stock.service;

import com.example.stock.model.Security;
import com.example.stock.model.Stock;
import com.example.stock.repository.SecurityRepository;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @Mock
    private SecurityRepository securityRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private SecurityService securityService;

    private Stock stock;
    private Security security1;
    private Security security2;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setId("AAPL");

        security1 = new Security();
        security1.setTicker("AAPL");
        security1.setType("STOCK");
        security1.setStock(stock);

        security2 = new Security();
        security2.setTicker("AAPL-OCT-2020-110-C");
        security2.setType("CALL");
        security2.setMaturity("2020-10-15");
        security2.setStrike(0.05);
        security2.setStock(stock);
    }

    @Test
    void testSaveSecurity() {
        when(securityRepository.save(any(Security.class))).thenReturn(security1);

        Security savedSecurity = securityService.saveSecurity(security1);

        assertNotNull(savedSecurity);
        assertEquals("AAPL", savedSecurity.getTicker());
        verify(securityRepository).save(security1);
    }

    @Test
    void testGetSecurity() {
        when(securityRepository.findById("AAPL")).thenReturn(Optional.of(security1));

        Optional<Security> result = securityService.getSecurity("AAPL");

        assertTrue(result.isPresent());
        assertEquals("AAPL", result.get().getTicker());
        verify(securityRepository).findById("AAPL");
    }

    @Test
    void testGetSecurityNotFound() {
        when(securityRepository.findById("INVALID")).thenReturn(Optional.empty());

        Optional<Security> result = securityService.getSecurity("INVALID");

        assertFalse(result.isPresent());
        verify(securityRepository).findById("INVALID");
    }

    @Test
    void testGetAllSecurities() {
        List<Security> securities = Arrays.asList(security1, security2);
        when(securityRepository.findAll()).thenReturn(securities);

        List<Security> result = securityService.getAllSecurities();

        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getTicker());
        assertEquals("AAPL-OCT-2020-110-C", result.get(1).getTicker());
        verify(securityRepository).findAll();
    }

    @Test
    void testGetSecuritiesByStock() {
        List<Security> securities = Arrays.asList(security1, security2);
        when(securityRepository.findByStockId("AAPL")).thenReturn(securities);

        List<Security> result = securityService.getSecuritiesByStock("AAPL");

        assertEquals(2, result.size());
        verify(securityRepository).findByStockId("AAPL");
    }

    @Test
    void testGetSecuritiesByType() {
        List<Security> securities = Arrays.asList(security1);
        when(securityRepository.findByType("STOCK")).thenReturn(securities);

        List<Security> result = securityService.getSecuritiesByType("STOCK");

        assertEquals(1, result.size());
        assertEquals("STOCK", result.get(0).getType());
        verify(securityRepository).findByType("STOCK");
    }

    @Test
    void testGetSecuritiesByStockAndType() {
        List<Security> securities = Arrays.asList(security2);
        when(securityRepository.findByStockIdAndType("AAPL", "CALL")).thenReturn(securities);

        List<Security> result = securityService.getSecuritiesByStockAndType("AAPL", "CALL");

        assertEquals(1, result.size());
        assertEquals("CALL", result.get(0).getType());
        verify(securityRepository).findByStockIdAndType("AAPL", "CALL");
    }

    @Test
    void testCreateSecurity() {
        when(stockRepository.findById("AAPL")).thenReturn(Optional.of(stock));
        when(securityRepository.save(any(Security.class))).thenReturn(security2);

        Security result = securityService.createSecurity("AAPL-OCT-2020-110-C", "AAPL", "CALL", "2020-10-15", 0.05);

        assertNotNull(result);
        assertEquals("AAPL-OCT-2020-110-C", result.getTicker());
        assertEquals("CALL", result.getType());
        verify(stockRepository).findById("AAPL");
        verify(securityRepository).save(any(Security.class));
    }

    @Test
    void testCreateSecurityStockNotFound() {
        when(stockRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            securityService.createSecurity("TEST", "INVALID", "STOCK", null, null);
        });

        verify(stockRepository).findById("INVALID");
        verify(securityRepository, never()).save(any(Security.class));
    }

    @Test
    void testDeleteSecurity() {
        doNothing().when(securityRepository).deleteById("AAPL");

        securityService.deleteSecurity("AAPL");

        verify(securityRepository).deleteById("AAPL");
    }
} 