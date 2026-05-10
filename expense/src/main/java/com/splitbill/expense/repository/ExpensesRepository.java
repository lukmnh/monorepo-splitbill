package com.splitbill.expense.repository;

import com.splitbill.common.constant.ExpensesType;
import com.splitbill.common.entity.projection.CategorySummary;
import com.splitbill.common.entity.projection.OwedSummary;
import com.splitbill.common.entity.projection.PaidSummary;
import com.splitbill.expense.entity.Expenses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Long> {
    Page<Expenses> findByGroupIdOrderByExpenseDateDesc(Long groupId, Pageable pageable);
    Page<Expenses> findByGroupIdAndCategory(Long groupId, ExpensesType category, Pageable pageable);
    @Query(value = """
            SELECT
                g.name                    AS groupName,
                e.paid_by_participant_id  AS participantId,
                e.paid_by_name            AS participantName,
                SUM(e.amount)             AS totalPaid,
                COUNT(e.id)               AS expenseCount
            FROM expenses.expenses e
            INNER JOIN groups.bill_groups g ON e.group_id = g.id
            WHERE e.group_id = :groupId
            GROUP BY g.name, e.paid_by_participant_id, e.paid_by_name
            ORDER BY totalPaid DESC
            """, nativeQuery = true)
    List<PaidSummary> findTotalPaidPerParticipant(@Param("groupId") Long groupId);
    @Query(value = """
            SELECT
                es.participant_id         AS participantId,
                es.participant_name       AS participantName,
                SUM(es.owed_amount)       AS totalOwed,
                COUNT(es.id)              AS expenseCount
            FROM expenses.expense_splits es
            JOIN expenses.expenses e ON e.id = es.expense_id
            WHERE e.group_id = :groupId
            GROUP BY es.participant_id, es.participant_name
            ORDER BY totalOwed DESC
            """, nativeQuery = true)
    List<OwedSummary> findTotalOwedPerParticipant(@Param("groupId") Long groupId);

    @Query(value = """
            SELECT
                e.category                AS category,
                SUM(e.amount)             AS total,
                COUNT(e.id)               AS count,
                AVG(e.amount)             AS average
            FROM expenses.expenses e
            WHERE e.group_id = :groupId
            GROUP BY e.category
            ORDER BY total DESC
            """, nativeQuery = true)
    List<CategorySummary> findExpenseBreakdownByCategory(@Param("groupId") Long groupId);
}
