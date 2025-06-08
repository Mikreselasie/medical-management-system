package com.example.model;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private Address address;

    private String idNumber;
    private LocalDate dateOfBirth;

    @ManyToMany
    private List<Diseases> diseases;

    private String patientId;

    @Enumerated(EnumType.STRING)
    private Strength painStrength;

    // No-arg constructor required by JPA
    public Patient() {}

    // Parameterized constructor
    public Patient(String name, int age, Gender gender, Address address, String idNumber, LocalDate dateOfBirth,
                   List<Diseases> diseases, String patientId, Strength painStrength, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.idNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
        this.diseases = diseases;
        this.patientId = patientId;
        this.painStrength = painStrength;
        if (this.address != null) {
            this.address.setPhoneNumber(phoneNumber);
        }
    }

    // Getters and setters ...

    public List<Diseases> getDiseases() {
        return diseases;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", idNumber='" + idNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", diseases=" + diseases +
                ", patientId='" + patientId + '\'' +
                ", painStrength=" + painStrength +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Address getAddress() {
        return address;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPatientId() {
        return patientId;
    }

    public Strength getPainStrength() {
        return painStrength;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setDiseases(List<Diseases> diseases) {
        this.diseases = diseases;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public void setPainStrength(Strength painStrength) {
        this.painStrength = painStrength;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
