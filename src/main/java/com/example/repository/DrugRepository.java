package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Drug;
import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Long> {
    List<Drug> findByQuantityGreaterThan(int quantity);
    List<Drug> findByNameContainingIgnoreCaseOrManufacturerContainingIgnoreCase(String name, String manufacturer);
} 