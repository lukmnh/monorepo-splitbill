package com.splitbill.settlement.service;

import com.splitbill.common.dto.settlement.response.SettlementRecordResponse;
import com.splitbill.common.dto.settlement.response.SummaryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SettlementService {
   SummaryResponse calculateForGroup(Long groupId);
    Page<SettlementRecordResponse> fetchSettlementHistory(Long groupId);
    List<SettlementRecordResponse> fetchLatestPerGroup();
}
