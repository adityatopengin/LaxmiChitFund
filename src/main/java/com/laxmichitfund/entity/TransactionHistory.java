package com.laxmichitfund.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links the transaction to the user who made it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "stock_symbol", nullable = false)
    private String stockSymbol;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "execution_price", nullable = false)
    private Double executionPrice; // The exact price at the moment the trade happened

    @Column(name = "trade_type", nullable = false)
    private String tradeType; // "BUY" or "SELL"

    @Column(name = "transaction_time", nullable = false, updatable = false)
    private LocalDateTime transactionTime;

    @PrePersist
    protected void onCreate() {
        this.transactionTime = LocalDateTime.now();
    }
}

