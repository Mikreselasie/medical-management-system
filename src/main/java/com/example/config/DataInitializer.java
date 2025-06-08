package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.model.Diseases;
import com.example.repository.DiseasesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
// CommandLineRunner is an interface provided by Spring Boot that allows you to execute code when the application starts up. It's particularly useful for tasks that need to run once during application initialization,
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private DiseasesRepository diseasesRepository;

    @Override
    public void run(String... args) {
        try {
            // Initialize diseases if the database is empty
            if (diseasesRepository.count() == 0) {
                logger.info("Initializing diseases in the database...");
                for (Diseases.DiseaseType type : Diseases.DiseaseType.values()) {
                    Diseases disease = new Diseases(type);
                    diseasesRepository.save(disease);
                    logger.info("Added disease: {}", type);
                }
                logger.info("Disease initialization completed. Total diseases: {}", diseasesRepository.count());
            } else {
                logger.info("Diseases already exist in the database. Total count: {}", diseasesRepository.count());
            }
        } catch (Exception e) {
            logger.error("Error initializing diseases: ", e);
        }
    }
} 