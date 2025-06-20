package com.example.model;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "patients")
public class Patient extends Person {
    @Column(unique = true)
    private String idNumber;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "patient_diseases",
        joinColumns = @JoinColumn(name = "patient_id"),
        inverseJoinColumns = @JoinColumn(name = "diseases_id")
    )
    private List<Diseases> diseases = new ArrayList<>();

    @Column(unique = true)
    private String patientId;

    @Enumerated(EnumType.STRING)
    private Strength painStrength = Strength.MEDIUM; // Default value

    // Default constructor
    public Patient() {
        super();
        this.diseases = new ArrayList<>();
        this.painStrength = Strength.MEDIUM;
    }

    // Parameterized constructor
    public Patient(String firstName, String lastName, int age, Gender gender, Address address, String idNumber, 
                  LocalDate dateOfBirth, List<Diseases> diseases, String patientId, 
                  Strength painStrength, String phoneNumber) {
        super(firstName, lastName, age, gender, address, phoneNumber);
        this.idNumber = idNumber;
        setDateOfBirth(dateOfBirth);
        this.diseases = diseases != null ? diseases : new ArrayList<>();
        this.patientId = patientId;
        this.painStrength = painStrength != null ? painStrength : Strength.MEDIUM;
    }

    // Helper methods for disease management
    public void addDisease(Diseases disease) {
        if (disease != null && !diseases.contains(disease)) {
            diseases.add(disease);
        }
    }

    public void removeDisease(Diseases disease) {
        if (disease != null) {
            diseases.remove(disease);
        }
    }

    public void clearDiseases() {
        diseases.clear();
    }

    // Getters and Setters
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public List<Diseases> getDiseases() {
        return diseases != null ? diseases : new ArrayList<>();
    }

    public void setDiseases(List<Diseases> diseases) {
        this.diseases = diseases != null ? diseases : new ArrayList<>();
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Strength getPainStrength() {
        return painStrength != null ? painStrength : Strength.MEDIUM;
    }

    public void setPainStrength(Strength painStrength) {
        this.painStrength = painStrength != null ? painStrength : Strength.MEDIUM;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + getFirstName() + " " + getLastName() + '\'' +
                ", age=" + getAge() +
                ", gender=" + getGender() +
                ", address=" + getAddress() +
                ", idNumber='" + idNumber + '\'' +
                ", dateOfBirth=" + getDateOfBirth() +
                ", diseases=" + diseases +
                ", patientId='" + patientId + '\'' +
                ", painStrength=" + painStrength +
                '}';
    }
}
