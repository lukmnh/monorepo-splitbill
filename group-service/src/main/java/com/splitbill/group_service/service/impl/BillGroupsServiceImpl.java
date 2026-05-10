package com.splitbill.group_service.service.impl;

import com.splitbill.common.constant.ErrorCode;
import com.splitbill.common.dto.billgroups.request.CreateSplitGroupsRequest;
import com.splitbill.common.dto.billgroups.response.ParticipantResponse;
import com.splitbill.common.dto.billgroups.response.SplitGroupsResponse;
import com.splitbill.common.exception.BusinessException;
import com.splitbill.group_service.entity.BillGroups;
import com.splitbill.group_service.entity.Participant;
import com.splitbill.group_service.repository.BillGroupsRepository;
import com.splitbill.group_service.repository.ParticipantRepository;
import com.splitbill.group_service.service.BillGroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillGroupsServiceImpl implements BillGroupsService {
    private final BillGroupsRepository billGroupsRepository;
    private final ParticipantRepository  participantRepository;

    @Override
    @Transactional
    public SplitGroupsResponse createGroup(CreateSplitGroupsRequest request) {
        BillGroups group = BillGroups.builder()
                .name(request.getName())
                .description(request.getDescription())
                .participants(new ArrayList<>())
                .build();

        if (request.getParticipants() != null) {
            request.getParticipants().forEach(dto -> {
                Participant p = Participant.builder()
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .joinedAt(LocalDateTime.now())
                        .group(group)
                        .build();
                group.getParticipants().add(p);
            });
        }
        BillGroups savedGroup = billGroupsRepository.save(group);

        return groupResponse(savedGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SplitGroupsResponse> fetchAllGroups(String search) {
        Page<BillGroups> groups;
        Pageable pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"));

        if (search != null && !search.isBlank()) {
            groups = billGroupsRepository.searchGroups(search, pageable);
        } else {
            groups = billGroupsRepository.findAll(pageable);
        }

        return groups.map(this::groupResponse);
    }

    @Override
    public SplitGroupsResponse detailGroup(Long id) {
        BillGroups groups = billGroupsRepository.findById(id).orElseThrow(() ->
                new BusinessException(ErrorCode.GROUP_NOT_FOUND.getCode(),
                        ErrorCode.GROUP_NOT_FOUND.getMessage(),
                        ErrorCode.GROUP_NOT_FOUND.getHttpStatus()));

        return toResponse(groups);
    }

    private SplitGroupsResponse groupResponse(BillGroups groups){
        return SplitGroupsResponse.builder()
                .name(groups.getName())
                .description(groups.getDescription())
                .participantCount(groups.getParticipants() != null ? groups.getParticipants().size() : 0)
                .createdAt(groups.getCreatedAt())
                .build();
    }

    private SplitGroupsResponse toResponse(BillGroups group) {
        return SplitGroupsResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .createdAt(group.getCreatedAt())
                .updatedAt(group.getUpdatedAt())
                .participantCount(group.getParticipants() != null ? group.getParticipants().size() : 0)
                .participants(group.getParticipants().stream()
                        .map(this::toParticipantResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private ParticipantResponse toParticipantResponse(Participant participant) {
        return ParticipantResponse.builder()
                .id(participant.getId())
                .name(participant.getName())
                .email(participant.getEmail())
                .joinedAt(participant.getJoinedAt())
                .build();
    }
}
