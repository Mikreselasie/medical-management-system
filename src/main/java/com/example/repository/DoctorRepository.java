package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Doctor;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);
    List<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    List<Doctor> findByIsActive(boolean isActive);
} 