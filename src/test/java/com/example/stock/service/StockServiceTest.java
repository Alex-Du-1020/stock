package com.example.stock.service;

import com.example.stock.model.Stock;
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
class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Stock stock1;
    private Stock stock2;

    @BeforeEach
    void setUp() {
        stock1 = new Stock();
        stock1.setId("AAPL");

        stock2 = new Stock();
        stock2.setId("TELSA");
    }

    @Test
    void testSaveStock() {
        when(stockRepository.save(any(Stock.class))).thenReturn(stock1);

        Stock savedStock = stockService.saveStock(stock1);

        assertNotNull(savedStock);
        assertEquals("AAPL", savedStock.getId());
        verify(stockRepository).save(stock1);
    }

    @Test
    void testGetStock() {
        when(stockRepository.findById("AAPL")).thenReturn(Optional.of(stock1));

        Optional<Stock> result = stockService.getStock("AAPL");

        assertTrue(result.isPresent());
        assertEquals("AAPL", result.get().getId());
        verify(stockRepository).findById("AAPL");
    }

    @Test
    void testGetStockNotFound() {
        when(stockRepository.findById("INVALID")).thenReturn(Optional.empty());

        Optional<Stock> result = stockService.getStock("INVALID");

        assertFalse(result.isPresent());
        verify(stockRepository).findById("INVALID");
    }

    @Test
    void testGetAllStocks() {
        List<Stock> stocks = Arrays.asList(stock1, stock2);
        when(stockRepository.findAll()).thenReturn(stocks);

        List<Stock> result = stockService.getAllStocks();

        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getId());
        assertEquals("TELSA", result.get(1).getId());
        verify(stockRepository).findAll();
    }

    @Test
    void testFindAll() {
        List<Stock> stocks = Arrays.asList(stock1, stock2);
        when(stockRepository.findAll()).thenReturn(stocks);

        List<Stock> result = stockService.findAll();

        assertEquals(2, result.size());
        assertEquals("AAPL", result.get(0).getId());
        assertEquals("TELSA", result.get(1).getId());
        verify(stockRepository).findAll();
    }

    @Test
    void testDeleteStock() {
        doNothing().when(stockRepository).deleteById("AAPL");

        stockService.deleteStock("AAPL");

        verify(stockRepository).deleteById("AAPL");
    }
} 