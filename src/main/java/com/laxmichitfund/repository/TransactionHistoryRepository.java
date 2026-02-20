package com.laxmichitfund.repository;

import com.laxmichitfund.entity.TransactionHistory;
import com.laxmichitfund.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    // Fetch all transactions for a user, sorted from newest to oldest
    List<TransactionHistory> findByUserOrderByTransactionTimeDesc(User user);
    
}

