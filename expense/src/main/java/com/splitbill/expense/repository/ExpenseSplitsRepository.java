package com.splitbill.expense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseSplitsRepository extends JpaRepository<ExpenseSplitsRepository, Long> {
}
