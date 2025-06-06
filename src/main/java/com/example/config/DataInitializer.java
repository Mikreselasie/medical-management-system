package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.model.Diseases;
import com.example.repository.DiseasesRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DiseasesRepository diseasesRepository;

    @Override
    public void run(String... args) {
        // Initialize diseases if the database is empty
        if (diseasesRepository.count() == 0) {
            for (Diseases.DiseaseType type : Diseases.DiseaseType.values()) {
                Diseases disease = new Diseases(type);
                diseasesRepository.save(disease);
            }
        }
    }
} 