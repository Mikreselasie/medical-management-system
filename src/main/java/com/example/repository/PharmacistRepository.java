package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Pharmacist;
import java.util.List;

public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    List<Pharmacist> findBySpecializationContainingIgnoreCase(String specialization);
    List<Pharmacist> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Pharmacist> findByIsActive(boolean isActive);
} 