package com.example.stock.repository;

import com.example.stock.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SecurityRepository extends JpaRepository<Security, String> {
    List<Security> findByStockId(String stockId);
    List<Security> findByType(String type);
    List<Security> findByStockIdAndType(String stockId, String type);
    List<Security> findAll();
} 