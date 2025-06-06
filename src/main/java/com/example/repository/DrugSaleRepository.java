package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.DrugSale;
import java.time.LocalDateTime;
import java.util.List;

public interface DrugSaleRepository extends JpaRepository<DrugSale, Long> {
    List<DrugSale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<DrugSale> findByDrugId(Long drugId);
} 