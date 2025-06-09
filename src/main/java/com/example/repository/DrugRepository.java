package com.example.repository;

import com.example.model.Drug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
    List<Drug> findByQuantityGreaterThan(int quantity);
    Page<Drug> findByNameContainingIgnoreCaseOrManufacturerContainingIgnoreCase(
        String name, String manufacturer, Pageable pageable);
} 