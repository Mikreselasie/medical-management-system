package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "drug_sales")
public class DrugSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;  // For optimistic locking

    @NotBlank(message = "Buyer name is required")
    @Size(min = 2, max = 100, message = "Buyer name must be between 2 and 100 characters")
    @Column(name = "buyer_name", nullable = false)
    private String buyerName;

    @NotBlank(message = "Buyer phone is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    @Column(name = "buyer_phone", nullable = false)
    private String buyerPhone;

    @NotNull(message = "Drug is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total amount must be greater than or equal to 0")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @NotNull(message = "Sale date is required")
    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;

    // Default constructor
    public DrugSale() {
        this.saleDate = LocalDateTime.now();
    }

    // Parameterized constructor
    public DrugSale(String buyerName, String buyerPhone, Drug drug, 
                   Integer quantity, BigDecimal totalAmount) {
        this.buyerName = buyerName;
        this.buyerPhone = buyerPhone;
        this.drug = drug;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.saleDate = LocalDateTime.now();
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

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity != null && quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        if (totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative");
        }
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    // Business logic methods
    public void calculateTotalAmount() {
        if (drug != null && quantity != null) {
            this.totalAmount = drug.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }

    // Equals and HashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrugSale drugSale = (DrugSale) o;
        return Objects.equals(id, drugSale.id) &&
                Objects.equals(version, drugSale.version) &&
                Objects.equals(buyerName, drugSale.buyerName) &&
                Objects.equals(buyerPhone, drugSale.buyerPhone) &&
                Objects.equals(drug, drugSale.drug) &&
                Objects.equals(quantity, drugSale.quantity) &&
                Objects.equals(totalAmount, drugSale.totalAmount) &&
                Objects.equals(saleDate, drugSale.saleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, buyerName, buyerPhone, drug, 
                          quantity, totalAmount, saleDate);
    }

    // ToString method
    @Override
    public String toString() {
        return "DrugSale{" +
                "id=" + id +
                ", version=" + version +
                ", buyerName='" + buyerName + '\'' +
                ", buyerPhone='" + buyerPhone + '\'' +
                ", drug=" + drug +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", saleDate=" + saleDate +
                '}';
    }
} 