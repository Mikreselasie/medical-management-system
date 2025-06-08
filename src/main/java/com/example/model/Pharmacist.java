package com.example.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "pharmacists")
public class Pharmacist extends Person {
    @NotBlank(message = "License number is required")
    @Size(min = 5, max = 20, message = "License number must be between 5 and 20 characters")
    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 100, message = "Qualification must be between 2 and 100 characters")
    private String qualification;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience must be greater than or equal to 0")
    @Max(value = 60, message = "Experience must be less than or equal to 60")
    private int experience;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "is_active")
    private boolean isActive = true;

    // Default constructor
    public Pharmacist() {
        super();
    }

    public Pharmacist(String firstName, String lastName, int age, Gender gender, Address address, String licenseNumber, String qualification, int experience, LocalDate joiningDate, String phoneNumber) {
        super(firstName, lastName, age, gender, address, phoneNumber);
        this.licenseNumber = licenseNumber;
        this.qualification = qualification;
        this.experience = experience;
        this.joiningDate = joiningDate;
    }

    // Getters and Setters
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
        return "Pharmacist{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", age=" + getAge() +
                ", gender=" + getGender() +
                ", address='" + getAddress() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", qualification='" + qualification + '\'' +
                ", experience=" + experience +
                ", joiningDate=" + joiningDate +
                ", isActive=" + isActive +
                '}';
    }
} 