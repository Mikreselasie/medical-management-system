package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Diseases;
import java.util.List;

public interface DiseasesRepository extends JpaRepository<Diseases, Long> {
    List<Diseases> findByDiseaseType(Diseases.DiseaseType diseaseType);
} 