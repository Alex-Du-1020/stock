package com.example.stock.repository;

import com.example.stock.model.Security;
import com.example.stock.model.SecurityQuantity;
import com.example.stock.model.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class SecurityQuantityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SecurityQuantityRepository securityQuantityRepository;

    private Stock stock;
    private Security security1;
    private Security security2;
    private SecurityQuantity securityQuantity1;
    private SecurityQuantity securityQuantity2;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setId("AAPL");
        entityManager.persistAndFlush(stock);

        security1 = new Security();
        security1.setTicker("AAPL");
        security1.setType("STOCK");
        security1.setStock(stock);
        entityManager.persistAndFlush(security1);

        security2 = new Security();
        security2.setTicker("AAPL-OCT-2020-110-C");
        security2.setType("CALL");
        security2.setStock(stock);
        entityManager.persistAndFlush(security2);

        securityQuantity1 = new SecurityQuantity();
        securityQuantity1.setSecurity(security1);
        securityQuantity1.setQuantity(1000);
        securityQuantity1.setCreatedAt(LocalDateTime.now());

        securityQuantity2 = new SecurityQuantity();
        securityQuantity2.setSecurity(security2);
        securityQuantity2.setQuantity(-20000);
        securityQuantity2.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSaveSecurityQuantity() {
        SecurityQuantity savedQuantity = securityQuantityRepository.save(securityQuantity1);

        assertNotNull(savedQuantity);
        assertNotNull(savedQuantity.getId());
        assertEquals(1000, savedQuantity.getQuantity());
        assertEquals("AAPL", savedQuantity.getSecurity().getTicker());
    }

    @Test
    void testFindById() {
        entityManager.persistAndFlush(securityQuantity1);

        Optional<SecurityQuantity> found = securityQuantityRepository.findById(securityQuantity1.getId());

        assertTrue(found.isPresent());
        assertEquals(1000, found.get().getQuantity());
        assertEquals("AAPL", found.get().getSecurity().getTicker());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<SecurityQuantity> found = securityQuantityRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        entityManager.persistAndFlush(securityQuantity1);
        entityManager.persistAndFlush(securityQuantity2);

        List<SecurityQuantity> quantities = securityQuantityRepository.findAll();

        assertEquals(2, quantities.size());
        assertTrue(quantities.stream().anyMatch(sq -> sq.getQuantity() == 1000));
        assertTrue(quantities.stream().anyMatch(sq -> sq.getQuantity() == -20000));
    }

    @Test
    void testFindQuantityByTicker() {
        entityManager.persistAndFlush(securityQuantity1);
        entityManager.persistAndFlush(securityQuantity2);

        Optional<Integer> quantity = securityQuantityRepository.findQuantityByTicker("AAPL");

        assertTrue(quantity.isPresent());
        assertEquals(1000, quantity.get());
    }

    @Test
    void testFindQuantityByTickerNotFound() {
        Optional<Integer> quantity = securityQuantityRepository.findQuantityByTicker("INVALID");

        assertFalse(quantity.isPresent());
    }

    @Test
    void testFindByTicker() {
        entityManager.persistAndFlush(securityQuantity1);
        entityManager.persistAndFlush(securityQuantity2);

        Optional<SecurityQuantity> found = securityQuantityRepository.findByTicker("AAPL");

        assertTrue(found.isPresent());
        assertEquals(1000, found.get().getQuantity());
        assertEquals("AAPL", found.get().getSecurity().getTicker());
    }

    @Test
    void testFindByTickerNotFound() {
        Optional<SecurityQuantity> found = securityQuantityRepository.findByTicker("INVALID");

        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById() {
        entityManager.persistAndFlush(securityQuantity1);

        securityQuantityRepository.deleteById(securityQuantity1.getId());

        Optional<SecurityQuantity> found = securityQuantityRepository.findById(securityQuantity1.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testExistsById() {
        entityManager.persistAndFlush(securityQuantity1);

        assertTrue(securityQuantityRepository.existsById(securityQuantity1.getId()));
        assertFalse(securityQuantityRepository.existsById(999L));
    }

    @Test
    void testCount() {
        entityManager.persistAndFlush(securityQuantity1);
        entityManager.persistAndFlush(securityQuantity2);

        long count = securityQuantityRepository.count();

        assertEquals(2, count);
    }

    @Test
    void testFindQuantityByTickerWithMultipleSecurities() {
        entityManager.persistAndFlush(securityQuantity1);
        entityManager.persistAndFlush(securityQuantity2);

        Optional<Integer> aaplQuantity = securityQuantityRepository.findQuantityByTicker("AAPL");
        Optional<Integer> callQuantity = securityQuantityRepository.findQuantityByTicker("AAPL-OCT-2020-110-C");

        assertTrue(aaplQuantity.isPresent());
        assertEquals(1000, aaplQuantity.get());

        assertTrue(callQuantity.isPresent());
        assertEquals(-20000, callQuantity.get());
    }
} 