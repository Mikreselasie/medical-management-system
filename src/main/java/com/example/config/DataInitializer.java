package com.example.config;

import com.example.model.Diseases;
import com.example.repository.DiseasesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
// CommandLineRunner is an interface provided by Spring Boot that allows you to execute code when the application starts up. It's particularly useful for tasks that need to run once during application initialization,
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private DiseasesRepository diseasesRepository;

    @Override
    public void run(String... args) {
        try {
            initializeDiseases();
        } catch (Exception e) {
            logger.error("Error during data initialization: {}", e.getMessage(), e);
        }
    }

    private void initializeDiseases() {
        try {
            if (diseasesRepository.count() == 0) {
                logger.info("Initializing diseases...");
                List<Diseases> diseases = new ArrayList<>();
                diseases.add(new Diseases(Diseases.DiseaseType.HYPERTENSION));
                diseases.add(new Diseases(Diseases.DiseaseType.DIABETES));
                diseases.add(new Diseases(Diseases.DiseaseType.ASTHMA));
                diseases.add(new Diseases(Diseases.DiseaseType.ARTHRITIS));
                diseases.add(new Diseases(Diseases.DiseaseType.HEART_DISEASE));
                diseases.add(new Diseases(Diseases.DiseaseType.BREAST_CANCER));
                diseases.add(new Diseases(Diseases.DiseaseType.CONGESTIVE_HEART_FAILURE));
                diseases.add(new Diseases(Diseases.DiseaseType.COPD));
                diseases.add(new Diseases(Diseases.DiseaseType.KIDNEY_FAILURE));
                diseases.add(new Diseases(Diseases.DiseaseType.PARKINSONS));
                
                diseasesRepository.saveAll(diseases);
                logger.info("Successfully initialized {} diseases", diseases.size());
            } else {
                logger.info("Diseases already initialized, skipping...");
            }
        } catch (Exception e) {
            logger.error("Error initializing diseases: {}", e.getMessage(), e);
            throw e;
        }
    }
} 