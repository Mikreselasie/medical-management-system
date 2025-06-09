// Main application class for the Medical Management System
// This is the entry point of the Spring Boot application
package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication  // Marks this as a Spring Boot application and enables auto-configuration
@EntityScan("com.example.model")
public class Application {

    // Main method that starts the Spring Boot application
    public static void main(String[] args) {
        // Launches the application and creates the Spring application context
        SpringApplication.run(Application.class, args);
    }
} 