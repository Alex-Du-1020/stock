package com.example.stock.repository;

import com.example.stock.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StockRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StockRepository stockRepository;

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
        Stock savedStock = stockRepository.save(stock1);

        assertNotNull(savedStock);
        assertEquals("AAPL", savedStock.getId());
    }

    @Test
    void testFindById() {
        entityManager.persistAndFlush(stock1);

        Optional<Stock> found = stockRepository.findById("AAPL");

        assertTrue(found.isPresent());
        assertEquals("AAPL", found.get().getId());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Stock> found = stockRepository.findById("INVALID");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        entityManager.persistAndFlush(stock1);
        entityManager.persistAndFlush(stock2);

        List<Stock> stocks = stockRepository.findAll();

        assertEquals(2, stocks.size());
        assertTrue(stocks.stream().anyMatch(s -> s.getId().equals("AAPL")));
        assertTrue(stocks.stream().anyMatch(s -> s.getId().equals("TELSA")));
    }

    @Test
    void testDeleteById() {
        entityManager.persistAndFlush(stock1);

        stockRepository.deleteById("AAPL");

        Optional<Stock> found = stockRepository.findById("AAPL");
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsById() {
        entityManager.persistAndFlush(stock1);

        assertTrue(stockRepository.existsById("AAPL"));
        assertFalse(stockRepository.existsById("INVALID"));
    }

    @Test
    void testCount() {
        entityManager.persistAndFlush(stock1);
        entityManager.persistAndFlush(stock2);

        long count = stockRepository.count();

        assertEquals(2, count);
    }
} 