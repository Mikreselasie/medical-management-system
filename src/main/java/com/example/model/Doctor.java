package com.example.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "doctors")
public class Doctor extends Person {
    @NotBlank(message = "Specialization is required")
    @Size(min = 2, max = 100, message = "Specialization must be between 2 and 100 characters")
    @Column(name = "specialization")
    private String specialization;

    @NotBlank(message = "License number is required")
    @Size(min = 5, max = 50, message = "License number must be between 5 and 50 characters")
    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 100, message = "Qualification must be between 2 and 100 characters")
    @Column(name = "qualification")
    private String qualification;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 100, message = "Experience cannot exceed 100 years")
    @Column(name = "experience")
    private int experience;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "is_active")
    private boolean isActive = true;  // Default to true for new doctors

    // Default constructor
    public Doctor() {
        super();
    }

    // Parameterized constructor
    public Doctor(String firstName, String lastName, int age, Gender gender, Address address, String licenseNumber, String qualification, int experience, String specialization, LocalDate joiningDate, String phoneNumber) {
        super(firstName, lastName, age, gender, address, phoneNumber);
        this.licenseNumber = licenseNumber;
        this.qualification = qualification;
        this.experience = experience;
        this.specialization = specialization;
        this.joiningDate = joiningDate;
    }

    // Getters and Setters
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", specialization='" + specialization + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", qualification='" + qualification + '\'' +
                ", experience=" + experience +
                ", joiningDate=" + joiningDate +
                ", isActive=" + isActive +
                '}';
    }
} 