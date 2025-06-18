package com.example.stock.repository;

import com.example.stock.model.SecurityQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SecurityQuantityRepository extends JpaRepository<SecurityQuantity, Long> {
    
    @Query("SELECT sq.quantity FROM SecurityQuantity sq WHERE sq.security.ticker = :ticker")
    Optional<Integer> findQuantityByTicker(@Param("ticker") String ticker);
    
    @Query("SELECT sq FROM SecurityQuantity sq WHERE sq.security.ticker = :ticker")
    Optional<SecurityQuantity> findByTicker(@Param("ticker") String ticker);
} 