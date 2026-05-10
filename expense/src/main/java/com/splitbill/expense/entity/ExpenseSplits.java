package com.splitbill.expense.entity;

import com.splitbill.common.constant.ExpensesType;
import com.splitbill.common.constant.SplitType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "expense_splits", schema = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ExpenseSplits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id", nullable = false)
    private Expenses expense;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @Column(name = "participant_name", nullable = false, length = 100)
    private String participantName;

    @Column(name = "owed_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal owedAmount;

    @Column(name = "share_value", precision = 15, scale = 4)
    private BigDecimal shareValue;
}
