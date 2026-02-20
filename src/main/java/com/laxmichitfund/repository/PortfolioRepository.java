package com.laxmichitfund.repository;

import com.laxmichitfund.entity.Portfolio;
import com.laxmichitfund.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // Fetch all stocks owned by a specific user to display on their Dashboard
    List<Portfolio> findAllByUser(User user);

    // Check if a user already owns a specific stock (useful when they buy more or try to sell)
    // Spring translates this to: SELECT * FROM portfolios WHERE user_id = ? AND stock_symbol = ?
    Optional<Portfolio> findByUserAndStockSymbol(User user, String stockSymbol);
    
}

