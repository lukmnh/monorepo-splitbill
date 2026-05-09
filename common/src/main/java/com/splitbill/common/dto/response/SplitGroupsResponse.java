package com.splitbill.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SplitGroupsResponse {
    private Long id;
    private String name;
    private String description;
    private int participantCount;
    private List<ParticipantResponse> participants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
