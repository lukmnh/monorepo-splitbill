package com.splitbill.settlement.controller;

import com.splitbill.common.dto.settlement.response.SettlementRecordResponse;
import com.splitbill.common.dto.settlement.response.SummaryResponse;
import com.splitbill.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<SummaryResponse> getSettlement(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(settlementService.calculateForGroup(groupId));
    }

    @GetMapping("/groups/{groupId}/history")
    public ResponseEntity<Page<SettlementRecordResponse>> getHistory(@PathVariable Long groupId) {
        return ResponseEntity.ok(settlementService.fetchSettlementHistory(groupId));
    }

    @GetMapping("/latest")
    public ResponseEntity<List<SettlementRecordResponse>> getLatestPerGroup() {
        return ResponseEntity.ok(settlementService.fetchLatestPerGroup());
    }
}
