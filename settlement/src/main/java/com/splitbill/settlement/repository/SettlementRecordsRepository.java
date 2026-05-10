package com.splitbill.settlement.repository;

import com.splitbill.common.dto.settlement.response.SettlementRecordResponse;
import com.splitbill.settlement.entity.SettlementRecords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettlementRecordsRepository extends JpaRepository<SettlementRecords, Long> {
    Page<SettlementRecords> findByGroupId(Long groupId, Pageable pageable);
    @Query(value = """
            SELECT sr.*
            FROM settlement_records sr
            INNER JOIN (
                SELECT group_id, MAX(calculated_at) AS latest
                FROM settlement_records
                GROUP BY group_id
            ) latest_sr ON sr.group_id = latest_sr.group_id
                        AND sr.calculated_at = latest_sr.latest
            ORDER BY sr.calculated_at DESC
            """, nativeQuery = true)
    List<SettlementRecords> findLatestPerGroup();
}
