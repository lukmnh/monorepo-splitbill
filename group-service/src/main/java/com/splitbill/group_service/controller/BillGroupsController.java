package com.splitbill.group_service.controller;

import com.splitbill.common.dto.billgroups.request.CreateSplitGroupsRequest;
import com.splitbill.common.dto.billgroups.response.SplitGroupsResponse;
import com.splitbill.group_service.service.BillGroupsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<Page<SplitGroupsResponse>> getAllGroups(
            @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.ok(billGroupsService.fetchAllGroups(search));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<SplitGroupsResponse> detail(@PathVariable("groupId") Long groupId) {
        return ResponseEntity.ok(billGroupsService.detailGroup(groupId));
    }

}
