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
                    
                    // Special handling for "CANCER" type
                    if (disease.getDiseaseType().toString().equals("CANCER")) {
                        logger.info("Converting generic CANCER type to BREAST_CANCER");
                        disease.setDiseaseType(Diseases.DiseaseType.BREAST_CANCER);
                    } else {
                        // Set to UNKNOWN type for other invalid types
                        disease.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                    }
                    
                    try {
                        diseasesRepository.save(disease);
                        logger.info("Successfully fixed invalid disease type: {}", disease);
                    } catch (Exception saveEx) {
                        logger.error("Failed to save fixed disease: {}", disease, saveEx);
                    }
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
                        try {
                            Diseases newDisease = new Diseases(type);
                            diseases.add(newDisease);
                            logger.debug("Added disease type: {}", type);
                        } catch (Exception e) {
                            logger.error("Failed to create disease for type: {}", type, e);
                        }
                    }
                }
                
                try {
                    diseasesRepository.saveAll(diseases);
                    logger.info("Successfully initialized {} diseases", diseases.size());
                } catch (Exception e) {
                    logger.error("Failed to save diseases: {}", e.getMessage(), e);
                    throw e;
                }
            } else {
                logger.info("Diseases already initialized, skipping...");
            }
        } catch (Exception e) {
            logger.error("Error initializing diseases: {}", e.getMessage(), e);
            // Don't throw the exception, just log it
            // This allows the application to continue even if disease initialization fails
            logger.warn("Continuing application startup despite disease initialization error");
        }
    }
} 