package com.laxmichitfund.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data // Lombok automatically generates Getters, Setters, and toString()
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // This represents the dummy money given to the user to trade
    @Column(name = "virtual_cash", nullable = false)
    private Double virtualCash;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Automatically set the timestamp and starting balance before saving
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.virtualCash == null) {
            this.virtualCash = 100000.00; // Give users ₹1,00,000 dummy cash on signup
        }
    }
}

