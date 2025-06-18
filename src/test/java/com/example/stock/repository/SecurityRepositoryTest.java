package com.example.stock.repository;

import com.example.stock.model.Security;
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
class SecurityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SecurityRepository securityRepository;

    private Stock stock;
    private Security security1;
    private Security security2;
    private Security security3;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setId("AAPL");
        entityManager.persistAndFlush(stock);

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

        security3 = new Security();
        security3.setTicker("AAPL-OCT-2020-110-P");
        security3.setType("PUT");
        security3.setMaturity("2020-10-15");
        security3.setStrike(0.005);
        security3.setStock(stock);
    }

    @Test
    void testSaveSecurity() {
        Security savedSecurity = securityRepository.save(security1);

        assertNotNull(savedSecurity);
        assertEquals("AAPL", savedSecurity.getTicker());
        assertEquals("STOCK", savedSecurity.getType());
    }

    @Test
    void testFindById() {
        entityManager.persistAndFlush(security1);

        Optional<Security> found = securityRepository.findById("AAPL");

        assertTrue(found.isPresent());
        assertEquals("AAPL", found.get().getTicker());
        assertEquals("STOCK", found.get().getType());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Security> found = securityRepository.findById("INVALID");

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        entityManager.persistAndFlush(security1);
        entityManager.persistAndFlush(security2);
        entityManager.persistAndFlush(security3);

        List<Security> securities = securityRepository.findAll();

        assertEquals(3, securities.size());
        assertTrue(securities.stream().anyMatch(s -> s.getTicker().equals("AAPL")));
        assertTrue(securities.stream().anyMatch(s -> s.getTicker().equals("AAPL-OCT-2020-110-C")));
        assertTrue(securities.stream().anyMatch(s -> s.getTicker().equals("AAPL-OCT-2020-110-P")));
    }

    @Test
    void testFindByStockId() {
        entityManager.persistAndFlush(security1);
        entityManager.persistAndFlush(security2);
        entityManager.persistAndFlush(security3);

        List<Security> securities = securityRepository.findByStockId("AAPL");

        assertEquals(3, securities.size());
        assertTrue(securities.stream().allMatch(s -> s.getStock().getId().equals("AAPL")));
    }

    @Test
    void testFindByType() {
        entityManager.persistAndFlush(security1);
        entityManager.persistAndFlush(security2);
        entityManager.persistAndFlush(security3);

        List<Security> callOptions = securityRepository.findByType("CALL");

        assertEquals(1, callOptions.size());
        assertEquals("CALL", callOptions.get(0).getType());
        assertEquals("AAPL-OCT-2020-110-C", callOptions.get(0).getTicker());
    }

    @Test
    void testFindByStockIdAndType() {
        entityManager.persistAndFlush(security1);
        entityManager.persistAndFlush(security2);
        entityManager.persistAndFlush(security3);

        List<Security> aaplCalls = securityRepository.findByStockIdAndType("AAPL", "CALL");

        assertEquals(1, aaplCalls.size());
        assertEquals("CALL", aaplCalls.get(0).getType());
        assertEquals("AAPL", aaplCalls.get(0).getStock().getId());
        assertEquals("AAPL-OCT-2020-110-C", aaplCalls.get(0).getTicker());
    }

    @Test
    void testDeleteById() {
        entityManager.persistAndFlush(security1);

        securityRepository.deleteById("AAPL");

        Optional<Security> found = securityRepository.findById("AAPL");
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsById() {
        entityManager.persistAndFlush(security1);

        assertTrue(securityRepository.existsById("AAPL"));
        assertFalse(securityRepository.existsById("INVALID"));
    }

    @Test
    void testCount() {
        entityManager.persistAndFlush(security1);
        entityManager.persistAndFlush(security2);

        long count = securityRepository.count();

        assertEquals(2, count);
    }
} 