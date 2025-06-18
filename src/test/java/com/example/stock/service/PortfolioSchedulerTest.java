package com.example.stock.service;

import com.example.stock.model.Security;
import com.example.stock.model.Stock;
import com.example.stock.util.MarketDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PortfolioSchedulerTest {

    @Mock
    private MarketDataProvider marketDataProvider;

    @Mock
    private SecurityQuantityService securityQuantityService;

    @Mock
    private SecurityService securityService;

    @Mock
    private StockService stockService;

    @InjectMocks
    private PortfolioScheduler portfolioScheduler;

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
        security1.setStrike(null);

        security2 = new Security();
        security2.setTicker("AAPL-OCT-2020-110-C");
        security2.setType("CALL");
        security2.setStock(stock);
        security2.setStrike(0.05);
    }

    @Test
    void testCalculatePortfolioValue() {
        // Mock stock service
        List<Stock> stocks = Arrays.asList(stock);
        when(stockService.findAll()).thenReturn(stocks);

        // Mock market data provider
        when(marketDataProvider.getCurrentPrice("AAPL")).thenReturn(100.0);

        // Mock security service
        List<Security> securities = Arrays.asList(security1, security2);
        when(securityService.findAll()).thenReturn(securities);

        // Mock security quantity service
        when(securityQuantityService.getQuantityByTicker("AAPL")).thenReturn(Optional.of(1000));
        when(securityQuantityService.getQuantityByTicker("AAPL-OCT-2020-110-C")).thenReturn(Optional.of(-20000));

        // Execute the method
        portfolioScheduler.calculatePortfolioValue();

        // Verify interactions
        verify(stockService).findAll();
        verify(marketDataProvider).getCurrentPrice("AAPL");
        verify(securityService).findAll();
        verify(securityQuantityService).getQuantityByTicker("AAPL");
        verify(securityQuantityService).getQuantityByTicker("AAPL-OCT-2020-110-C");
    }

    @Test
    void testCalculatePortfolioValueWithMissingQuantity() {
        // Mock stock service
        List<Stock> stocks = Arrays.asList(stock);
        when(stockService.findAll()).thenReturn(stocks);

        // Mock market data provider
        when(marketDataProvider.getCurrentPrice("AAPL")).thenReturn(100.0);

        // Mock security service
        List<Security> securities = Arrays.asList(security1);
        when(securityService.findAll()).thenReturn(securities);

        // Mock security quantity service to return empty
        when(securityQuantityService.getQuantityByTicker("AAPL")).thenReturn(Optional.empty());

        // Execute the method
        portfolioScheduler.calculatePortfolioValue();

        // Verify interactions
        verify(stockService).findAll();
        verify(marketDataProvider).getCurrentPrice("AAPL");
        verify(securityService).findAll();
        verify(securityQuantityService).getQuantityByTicker("AAPL");
    }

    @Test
    void testGetStockPrices() {
        // Mock stock service
        List<Stock> stocks = Arrays.asList(stock);
        when(stockService.findAll()).thenReturn(stocks);

        // Mock market data provider
        when(marketDataProvider.getCurrentPrice("AAPL")).thenReturn(150.0);

        // Execute the method (this is private, so we test it indirectly)
        portfolioScheduler.calculatePortfolioValue();

        // Verify the stock prices were retrieved
        verify(stockService).findAll();
        verify(marketDataProvider).getCurrentPrice("AAPL");
    }
} 