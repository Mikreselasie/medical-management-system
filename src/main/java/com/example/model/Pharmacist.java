package com.example.model;

public class Pharmacist extends Person {
    private String licenseNumber;
    private String qualification;
    private String experience;
    private String pharmacyName;
    private String workingHours;
    private String specialization;

    public Pharmacist(String name, String id, String licenseNumber) {
        super(name, id);
        this.licenseNumber = licenseNumber;
    }

    // Getters and Setters
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getPharmacyName() { return pharmacyName; }
    public void setPharmacyName(String pharmacyName) { this.pharmacyName = pharmacyName; }
    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    @Override
    public String toString() {
        return String.format("Pharmacist %s (License: %s)", getName(), licenseNumber);
    }
} 