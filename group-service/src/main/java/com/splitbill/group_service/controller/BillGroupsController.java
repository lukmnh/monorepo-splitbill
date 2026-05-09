package com.splitbill.group_service.controller;

import com.splitbill.common.dto.request.CreateSplitGroupsRequest;
import com.splitbill.common.dto.response.SplitGroupsResponse;
import com.splitbill.group_service.service.BillGroupsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/api/bill-groups")
@RequiredArgsConstructor
public class BillGroupsController {
    private final BillGroupsService billGroupsService;

    @PostMapping
    public ResponseEntity<SplitGroupsResponse> createGroup(
            @Valid @RequestBody CreateSplitGroupsRequest request) {
        SplitGroupsResponse response = billGroupsService.createGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
