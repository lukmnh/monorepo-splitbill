package com.splitbill.settlement.repository;

import com.splitbill.settlement.entity.SettlementRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRecordsRepository extends JpaRepository<SettlementRecords, Long> {
}
