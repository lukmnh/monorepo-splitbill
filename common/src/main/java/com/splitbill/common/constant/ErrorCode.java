package com.splitbill.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    GROUP_NOT_FOUND("GRP_404", "Group not found", HttpStatus.NOT_FOUND),
    DUPLICATE_PARTICIPANT("GRP_409", "Participant already exists in this group", HttpStatus.CONFLICT),
    PARTICIPANT_NOT_FOUND("PRT_404", "Participant not found in this group", HttpStatus.NOT_FOUND),
    INVALID_SPLIT_AMOUNT("EXP_400", "Exact split amounts must equal total expense", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
