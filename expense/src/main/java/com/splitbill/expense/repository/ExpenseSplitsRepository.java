package com.splitbill.expense.repository;

import com.splitbill.expense.entity.ExpenseSplits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseSplitsRepository extends JpaRepository<ExpenseSplits, Long> {
}
