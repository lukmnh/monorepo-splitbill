package com.splitbill.settlement.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.splitbill.common.constant.CategoryStatus;
import com.splitbill.common.constant.ErrorCode;
import com.splitbill.common.dto.expenses.response.CategorySummaryResponse;
import com.splitbill.common.dto.expenses.response.OwedSummaryResponse;
import com.splitbill.common.dto.expenses.response.PaidSummaryResponse;
import com.splitbill.common.dto.settlement.response.*;
import com.splitbill.common.exception.BusinessException;
import com.splitbill.settlement.client.ExpenseServiceClient;
import com.splitbill.settlement.entity.SettlementRecords;
import com.splitbill.settlement.repository.SettlementRecordsRepository;
import com.splitbill.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SettlementServiceImpl implements SettlementService {
    private final SettlementRecordsRepository settlementRecordsRepository;
    private final ObjectMapper objectMapper;
    private final ExpenseServiceClient  expenseServiceClient;

    @Override
    @Transactional
    public SummaryResponse calculateForGroup(Long groupId) {
        log.info("Starting settlement calculation for group: {}", groupId);

        var paidList = expenseServiceClient.getPaidSummary(groupId);
        log.info("response paid summary {} {}", groupId, paidList);
        var owedList = expenseServiceClient.getOwedSummary(groupId);
        log.info("response owed summary {} {}", groupId, owedList);
        var categoryList = expenseServiceClient.getCategorySummary(groupId);
        log.info("response category summary {} {}", groupId, categoryList);

        String groupName = paidList.stream()
                .findFirst()
                .map(PaidSummaryResponse::getGroupName).orElse(null);

        return calculate(groupId, groupName, paidList, owedList, categoryList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SettlementRecordResponse> fetchSettlementHistory(Long groupId) {
        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<SettlementRecords> entities = settlementRecordsRepository.findByGroupId(groupId, pageable);

        return entities.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<SettlementRecordResponse> fetchLatestPerGroup() {
        return settlementRecordsRepository.findLatestPerGroup()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private SummaryResponse calculate(Long groupId, String groupName,
                                            List<PaidSummaryResponse> paidList,
                                            List<OwedSummaryResponse> owedList,
                                            List<CategorySummaryResponse> categoryList) {

        Map<Long, ParticipantBalanceResponse> balanceMap = buildBalanceMap(paidList, owedList);

        List<ParticipantBalanceResponse> balances = balanceMap.values().stream()
                .peek(b -> {
                    BigDecimal net = b.getTotalPaid().subtract(b.getTotalOwed());
                    b.setNetBalance(net);
                    b.setStatus(CategoryStatus.fromNetBalance(net).getDesc());
                })
                .sorted(Comparator.comparing(ParticipantBalanceResponse::getNetBalance).reversed())
                .toList();

        BigDecimal totalExpenses = paidList.stream()
                .map(PaidSummaryResponse::getTotalPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<SettlementDebtResponse> settlements = minimizeCashFlow(balances);
        List<CategoryStatResponse> categoryStats = buildCategoryStats(categoryList, totalExpenses);

        SummaryResponse summary = SummaryResponse.builder()
                .groupId(groupId)
                .groupName(groupName)
                .totalExpenses(totalExpenses)
                .participantCount(balances.size())
                .transactionCount(settlements.size())
                .balances(balances)
                .settlements(settlements)
                .categoryStats(categoryStats)
                .calculatedAt(LocalDateTime.now())
                .build();

        persistSettlement(summary);
        return summary;
    }

    private Map<Long,ParticipantBalanceResponse> buildBalanceMap(
            List<PaidSummaryResponse> paidList,
            List<OwedSummaryResponse> owedList) {

        Map<Long, ParticipantBalanceResponse> map = paidList.stream()
                .collect(Collectors.toMap(
                        PaidSummaryResponse::getParticipantId,
                        p -> ParticipantBalanceResponse.builder()
                                .participantId(p.getParticipantId())
                                .name(p.getParticipantName())
                                .totalPaid(p.getTotalPaid())
                                .totalOwed(BigDecimal.ZERO)
                                .build(),
                        (a, b) -> a, LinkedHashMap::new));

        owedList.forEach(o -> map.compute(o.getParticipantId(), (id, existing) -> {
            var b = (existing != null) ? existing : ParticipantBalanceResponse.builder()
                                                    .participantId(o.getParticipantId())
                                                    .name(o.getParticipantName())
                                                    .totalPaid(BigDecimal.ZERO)
                                                    .build();
            b.setTotalOwed(o.getTotalOwed());
            return b;
        }));

        return map;
    }

    private List<SettlementDebtResponse> minimizeCashFlow(List<ParticipantBalanceResponse> balances) {
        Deque<long[]> creditors = balances.stream()
                .filter(b -> b.getNetBalance().signum() > 0)
                .map(b -> new long[]{b.getParticipantId(), b.getNetBalance().movePointRight(2).longValue()})
                .collect(Collectors.toCollection(ArrayDeque::new));

        Deque<long[]> debtors = balances.stream()
                .filter(b -> b.getNetBalance().signum() < 0)
                .map(b -> new long[]{b.getParticipantId(), b.getNetBalance().abs().movePointRight(2).longValue()})
                .collect(Collectors.toCollection(ArrayDeque::new));

        Map<Long, String> nameMap = balances.stream()
                .collect(Collectors.toMap(ParticipantBalanceResponse::getParticipantId, ParticipantBalanceResponse::getName));

        List<SettlementDebtResponse> debts = new ArrayList<>();
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            var creditor = creditors.pollFirst();
            var debtor = debtors.pollFirst();
            long amount = Math.min(creditor[1], debtor[1]);

            if (amount > 0) {
                debts.add(new SettlementDebtResponse(debtor[0], nameMap.get(debtor[0]),
                        creditor[0], nameMap.get(creditor[0]),
                        BigDecimal.valueOf(amount).movePointLeft(2)));
            }

            if (creditor[1] > amount) creditors.addFirst(new long[]{creditor[0], creditor[1] - amount});
            if (debtor[1] > amount) debtors.addFirst(new long[]{debtor[0], debtor[1] - amount});
        }
        return debts;
    }

    private void persistSettlement(SummaryResponse summary) {
        try {
            var record = SettlementRecords.builder()
                    .groupId(summary.getGroupId())
                    .groupName(summary.getGroupName())
                    .totalExpenses(summary.getTotalExpenses())
                    .participantCount(summary.getParticipantCount())
                    .transactionCount(summary.getTransactionCount())
                    .settlementJson(objectMapper.writeValueAsString(summary))
                    .build();
            settlementRecordsRepository.save(record);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize settlement JSON for group {}", summary.getGroupId(), e);
            throw new BusinessException(ErrorCode.FAILED_TO_INSERT_DATA);
        }
    }

    private List<CategoryStatResponse> buildCategoryStats(List<CategorySummaryResponse> list, BigDecimal total) {
        if (total.signum() == 0) return List.of();
        return list.stream()
                .map(c -> new CategoryStatResponse(c.getCategory(), c.getTotal(), c.getCount(),
                        c.getTotal().multiply(BigDecimal.valueOf(100)).divide(total, 1, RoundingMode.HALF_UP)))
                .sorted(Comparator.comparing(CategoryStatResponse::getTotal).reversed())
                .toList();
    }

    private SettlementRecordResponse convertToResponse(SettlementRecords entity) {
        return SettlementRecordResponse.builder()
                .id(entity.getId())
                .groupId(entity.getGroupId())
                .groupName(entity.getGroupName())
                .totalExpenses(entity.getTotalExpenses())
                .participantCount(entity.getParticipantCount())
                .transactionCount(entity.getTransactionCount())
                .build();
    }
}
