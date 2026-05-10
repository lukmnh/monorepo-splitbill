package com.splitbill.settlement.entity;

import com.splitbill.common.entity.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(name = "settlement_records", schema = "settlement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SettlementRecords extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "group_name", length = 100, nullable = false)
    private String groupName;

    @Column(name = "total_expenses", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalExpenses;

    @Column(name = "participant_count", nullable = false)
    private Integer participantCount;

    @Column(name = "transaction_count", nullable = false)
    private Integer transactionCount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "settlement_json", columnDefinition = "jsonb")
    private Object settlementJson;
}
