package com.example.model;

public class Doctor extends Person {
    private String specialization;
    private String licenseNumber;
    private String qualification;
    private String experience;
    private String department;
    private String workingHours;
    private String consultationFee;

    public Doctor(String name, String id, String specialization, String licenseNumber) {
        super(name, id);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }

    // Getters and Setters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
    public String getConsultationFee() { return consultationFee; }
    public void setConsultationFee(String consultationFee) { this.consultationFee = consultationFee; }

    @Override
    public String toString() {
        return String.format("Dr. %s - %s (License: %s)", getName(), specialization, licenseNumber);
    }
} 