package com.example.controller;

import java.time.LocalDate;
import java.util.List;

public class PatientForm {
    private String name;
    private int age;
    private String street;
    private String city;
    private String state;
    private String idNumber;
    private LocalDate dateOfBirth;
    private String[] diseases;
    private String painStrength;
    private String phoneNumber;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String[] getDiseases() { return diseases; }
    public void setDiseases(String[] diseases) { this.diseases = diseases; }
    
    public String getPainStrength() { return painStrength; }
    public void setPainStrength(String painStrength) { this.painStrength = painStrength; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
} 