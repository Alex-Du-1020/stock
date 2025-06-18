package com.example.stock.config;

import com.example.stock.service.SecurityQuantityService;
import com.example.stock.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ImportDataTest {

    @Mock
    private SecurityQuantityService securityQuantityService;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private ImportData importData;

    @Test
    void testImportPositionsBeanCreation() {
        CommandLineRunner runner = importData.importPositions();
        
        assertNotNull(runner);
    }

    @Test
    void testImportPositionsReturnsCommandLineRunner() {
        CommandLineRunner runner = importData.importPositions();
        
        assertNotNull(runner);
        // Verify it's a valid CommandLineRunner instance
        assertTrue(runner instanceof CommandLineRunner);
    }

    @Test
    void testImportPositionsBeanIsNotNull() {
        CommandLineRunner runner = importData.importPositions();
        
        assertNotNull(runner, "CommandLineRunner should not be null");
    }
} 