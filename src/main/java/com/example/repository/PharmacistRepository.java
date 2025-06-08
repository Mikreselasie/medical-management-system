package com.example.repository;

import com.example.model.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    List<Pharmacist> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Pharmacist> findByIsActive(boolean isActive);
} 