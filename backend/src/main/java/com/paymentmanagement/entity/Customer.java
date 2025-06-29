package com.paymentmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String phone;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @NotNull(message = "Date of amount taken is required")
    @Column(name = "date_of_amount_taken", nullable = false)
    private LocalDate dateOfAmountTaken;

    @Column(name = "day_of_amount_taken", nullable = false)
    private String dayOfAmountTaken;

    @Column(name = "weekly_amount", nullable = false)
    private Double weeklyAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    // Constructors
    public Customer() {}

    public Customer(String name, String phone, Double totalAmount, LocalDate dateOfAmountTaken) {
        this.name = name;
        this.phone = phone;
        this.totalAmount = totalAmount;
        this.dateOfAmountTaken = dateOfAmountTaken;
        this.dayOfAmountTaken = dateOfAmountTaken.getDayOfWeek().toString();
        this.weeklyAmount = Math.ceil(totalAmount / 10.0);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (dayOfAmountTaken == null && dateOfAmountTaken != null) {
            dayOfAmountTaken = dateOfAmountTaken.getDayOfWeek().toString();
        }
        if (weeklyAmount == null && totalAmount != null) {
            weeklyAmount = Math.ceil(totalAmount / 10.0);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (dateOfAmountTaken != null) {
            dayOfAmountTaken = dateOfAmountTaken.getDayOfWeek().toString();
        }
        if (totalAmount != null) {
            weeklyAmount = Math.ceil(totalAmount / 10.0);
        }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { 
        this.totalAmount = totalAmount;
        if (totalAmount != null) {
            this.weeklyAmount = Math.ceil(totalAmount / 10.0);
        }
    }

    public LocalDate getDateOfAmountTaken() { return dateOfAmountTaken; }
    public void setDateOfAmountTaken(LocalDate dateOfAmountTaken) { 
        this.dateOfAmountTaken = dateOfAmountTaken;
        if (dateOfAmountTaken != null) {
            this.dayOfAmountTaken = dateOfAmountTaken.getDayOfWeek().toString();
        }
    }

    public String getDayOfAmountTaken() { return dayOfAmountTaken; }
    public void setDayOfAmountTaken(String dayOfAmountTaken) { this.dayOfAmountTaken = dayOfAmountTaken; }

    public Double getWeeklyAmount() { return weeklyAmount; }
    public void setWeeklyAmount(Double weeklyAmount) { this.weeklyAmount = weeklyAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
}