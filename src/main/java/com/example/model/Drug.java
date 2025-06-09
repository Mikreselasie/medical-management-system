package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "drugs")
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Drug name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Manufacturer is required")
    @Column(nullable = false)
    private String manufacturer;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Column(nullable = false)
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @Column(nullable = false)
    private Integer quantity;

    @Future(message = "Expiry date must be in the future")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @NotNull(message = "Drug category is required")
    @Column(name = "drug_category")
    @Enumerated(EnumType.STRING)
    private DrugCategory drugCategory;

    @Column(name = "side_effects")
    private String sideEffects;

    @Column
    private String strength;

    @Column
    private String description;

    @NotNull(message = "Disease is required")
    @ManyToOne
    @JoinColumn(name = "disease_id")
    private Diseases disease;

    // No-arg constructor required by JPA
    public Drug() {}

    // Parameterized constructor
    public Drug(String name, String manufacturer, LocalDate expiryDate, 
                double price, int quantity, String description, String sideEffects,
                String strength, DrugCategory drugCategory, Diseases disease) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.price = BigDecimal.valueOf(price);
        this.quantity = quantity;
        this.description = description;
        this.sideEffects = sideEffects;
        this.strength = strength;
        this.drugCategory = drugCategory;
        this.disease = disease;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public DrugCategory getDrugCategory() {
        return drugCategory;
    }

    public void setDrugCategory(DrugCategory drugCategory) {
        this.drugCategory = drugCategory;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Diseases getDisease() {
        return disease;
    }

    public void setDisease(Diseases disease) {
        this.disease = disease;
    }
} 