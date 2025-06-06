package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Diseases;
import java.util.Optional;

public interface DiseasesRepository extends JpaRepository<Diseases, Long> {
    Optional<Diseases> findByDiseaseType(Diseases.DiseaseType diseaseType);
} 