package com.laxmichitfund.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Links this portfolio entry to a specific user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "stock_symbol", nullable = false)
    private String stockSymbol; // e.g., "RELIANCE"

    @Column(nullable = false)
    private Integer quantity; // How many shares they own

    @Column(name = "average_buy_price", nullable = false)
    private Double averageBuyPrice; // Used to calculate P&L

    // Automatically update the average buy price when new stocks are bought
    public void updateHolding(int additionalQuantity, double newPurchasePrice) {
        double totalCostBefore = this.quantity * this.averageBuyPrice;
        double newCost = additionalQuantity * newPurchasePrice;
        
        this.quantity += additionalQuantity;
        this.averageBuyPrice = (totalCostBefore + newCost) / this.quantity;
    }
}

