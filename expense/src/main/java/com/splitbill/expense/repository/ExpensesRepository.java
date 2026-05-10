package com.splitbill.expense.repository;

import com.splitbill.common.constant.ExpensesType;
import com.splitbill.expense.entity.Expenses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    Page<Expenses> findByGroupIdOrderByExpenseDateDesc(Long groupId, Pageable pageable);
    Page<Expenses> findByGroupIdAndCategory(Long groupId, ExpensesType category, Pageable pageable);
}
