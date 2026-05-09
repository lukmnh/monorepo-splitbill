package com.splitbill.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime joinedAt;
}
