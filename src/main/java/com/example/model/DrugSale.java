package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DrugSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drug_id")
    private Drug drug;

    private int quantity;
    private double totalPrice;
    private LocalDateTime saleDate;
    private String customerName;
    private String customerPhone;

    // No-arg constructor required by JPA
    public DrugSale() {}

    // Parameterized constructor
    public DrugSale(Drug drug, int quantity, String customerName, String customerPhone) {
        this.drug = drug;
        this.quantity = quantity;
        this.totalPrice = drug.getPrice() * quantity;
        this.saleDate = LocalDateTime.now();
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (this.drug != null) {
            this.totalPrice = this.drug.getPrice() * quantity;
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
} 