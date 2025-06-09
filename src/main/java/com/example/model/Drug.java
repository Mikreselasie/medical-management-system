// Entity class representing drugs in the medical system
// Maps to the 'drugs' table in the database
package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "drugs")
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;  // For optimistic locking

    @NotBlank(message = "Drug name is required")
    @Size(min = 2, max = 100, message = "Drug name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Manufacturer is required")
    @Size(min = 2, max = 100, message = "Manufacturer name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String manufacturer;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @Column(nullable = false)
    private Integer quantity = 0;

    @Future(message = "Expiry date must be in the future")
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @NotNull(message = "Drug category is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "drug_category", nullable = false)
    private DrugCategory drugCategory = DrugCategory.OTHER;

    @Size(max = 1000, message = "Side effects description cannot exceed 1000 characters")
    @Column(name = "side_effects", length = 1000)
    private String sideEffects;

    @Pattern(regexp = "^[0-9]+(\\.[0-9]+)?\\s*(mg|g|ml|L|IU)$", 
             message = "Strength must be in format: number followed by unit (e.g., 500mg, 1g, 100ml)")
    @Column
    private String strength;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Disease is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id", nullable = false)
    private Diseases disease;

    // Default constructor required by JPA
    public Drug() {
        this.price = BigDecimal.ZERO;
        this.quantity = 0;
        this.drugCategory = DrugCategory.OTHER;
    }

    // Parameterized constructor with validation
    public Drug(String name, String manufacturer, LocalDate expiryDate, 
                BigDecimal price, Integer quantity, String description, 
                String sideEffects, String strength, DrugCategory drugCategory, 
                Diseases disease) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.price = price != null ? price : BigDecimal.ZERO;
        this.quantity = quantity != null ? quantity : 0;
        this.description = description;
        this.sideEffects = sideEffects;
        this.strength = strength;
        this.drugCategory = drugCategory != null ? drugCategory : DrugCategory.OTHER;
        this.disease = disease;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date must be in the future");
        }
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

    // Business logic methods
    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(LocalDate.now());
    }

    public boolean isLowStock(int threshold) {
        return quantity != null && quantity <= threshold;
    }

    // Equals and HashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drug drug = (Drug) o;
        return Objects.equals(id, drug.id) &&
                Objects.equals(version, drug.version) &&
                Objects.equals(name, drug.name) &&
                Objects.equals(manufacturer, drug.manufacturer) &&
                Objects.equals(price, drug.price) &&
                Objects.equals(quantity, drug.quantity) &&
                Objects.equals(expiryDate, drug.expiryDate) &&
                drugCategory == drug.drugCategory &&
                Objects.equals(sideEffects, drug.sideEffects) &&
                Objects.equals(strength, drug.strength) &&
                Objects.equals(description, drug.description) &&
                Objects.equals(disease, drug.disease);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, name, manufacturer, price, quantity, 
                          expiryDate, drugCategory, sideEffects, strength, 
                          description, disease);
    }

    // ToString method
    @Override
    public String toString() {
        return "Drug{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", drugCategory=" + drugCategory +
                '}';
    }
} 