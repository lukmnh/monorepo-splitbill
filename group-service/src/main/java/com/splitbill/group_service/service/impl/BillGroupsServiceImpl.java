package com.splitbill.group_service.service.impl;

import com.splitbill.common.dto.request.CreateSplitGroupsRequest;
import com.splitbill.common.dto.response.ParticipantResponse;
import com.splitbill.common.dto.response.SplitGroupsResponse;
import com.splitbill.group_service.entity.BillGroups;
import com.splitbill.group_service.entity.Participant;
import com.splitbill.group_service.repository.BillGroupsRepository;
import com.splitbill.group_service.repository.ParticipantRepository;
import com.splitbill.group_service.service.BillGroupsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                .build();

        BillGroups savedGroup = billGroupsRepository.save(group);

        List<Participant> participants = request.getParticipants().stream()
                .map(dto -> Participant.builder()
                        .name(dto.getName())
                        .email(dto.getEmail())
                        .joinedAt(LocalDateTime.now())
                        .group(savedGroup)
                        .build())
                .collect(Collectors.toList());

        participantRepository.saveAll(participants);
        savedGroup.setParticipants(participants);

        return toResponse(savedGroup);
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
