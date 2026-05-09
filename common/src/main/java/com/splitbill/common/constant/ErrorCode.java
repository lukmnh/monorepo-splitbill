package com.splitbill.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    GROUP_NOT_FOUND("GRP_001", "Group not found", HttpStatus.NOT_FOUND),
    DUPLICATE_PARTICIPANT("GRP_002", "Participant already exists in this group", HttpStatus.CONFLICT);
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
