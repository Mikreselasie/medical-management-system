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
            // First, check for and fix any invalid disease types
            List<Diseases> existingDiseases = diseasesRepository.findAll();
            boolean hasInvalidTypes = false;
            
            for (Diseases disease : existingDiseases) {
                try {
                    // This will throw an exception if the disease type is invalid
                    disease.getDiseaseType();
                } catch (Exception e) {
                    logger.warn("Found invalid disease type in database: {}", disease);
                    hasInvalidTypes = true;
                    // Set to UNKNOWN type
                    disease.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                    diseasesRepository.save(disease);
                }
            }
            
            if (hasInvalidTypes) {
                logger.info("Fixed invalid disease types in database");
            }

            // Then proceed with initialization if needed
            if (diseasesRepository.count() == 0) {
                logger.info("Initializing diseases...");
                List<Diseases> diseases = new ArrayList<>();
                
                // Add all disease types
                for (Diseases.DiseaseType type : Diseases.DiseaseType.values()) {
                    if (type != Diseases.DiseaseType.UNKNOWN) {  // Skip UNKNOWN type
                        diseases.add(new Diseases(type));
                    }
                }
                
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