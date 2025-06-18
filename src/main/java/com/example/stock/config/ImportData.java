package com.example.stock.config;

import com.example.stock.model.Position;
import com.example.stock.model.SecurityQuantity;
import com.example.stock.service.SecurityQuantityService;
import com.example.stock.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class ImportData {

    @Autowired
    private SecurityQuantityService securityQuantityService;
    
    @Autowired
    private SecurityService securityService;

    @Bean
    public CommandLineRunner importPositions() {
        return args -> {
            List<Position> positions = new ArrayList<>();
            
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new ClassPathResource("static/position.csv").getInputStream()))) {
                
                // Skip header line
                String line = reader.readLine();
                
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    Position position = new Position();
                    position.setSymbol(values[0]);
                    position.setPositionSize(Integer.parseInt(values[1]));
                    positions.add(position);
                }
                
                for (Position position : positions) {
                    // Check if security exists
                    Optional<com.example.stock.model.Security> securityOpt = 
                        securityService.getSecurity(position.getSymbol());
                    
                    if (securityOpt.isPresent()) {
                        SecurityQuantity securityQuantity = new SecurityQuantity();
                        securityQuantity.setSecurity(securityOpt.get());
                        securityQuantity.setQuantity(position.getPositionSize());
                        securityQuantity.setCreatedAt(LocalDateTime.now());
                        
                        securityQuantityService.saveSecurityQuantity(securityQuantity);
                    } else {
                        System.out.println("Warning: Security not found for ticker: " + position.getSymbol());
                    }
                }
                System.out.println("Successfully created " + positions.size() + " SECURITY_QUANTITY records");
                
            } catch (Exception e) {
                System.err.println("Error reading position.csv: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
} 