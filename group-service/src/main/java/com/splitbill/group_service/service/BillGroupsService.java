package com.splitbill.group_service.service;

import com.splitbill.common.dto.billgroups.request.CreateSplitGroupsRequest;
import com.splitbill.common.dto.billgroups.response.SplitGroupsResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BillGroupsService {
    SplitGroupsResponse createGroup(@Valid @RequestBody CreateSplitGroupsRequest request);
    Page<SplitGroupsResponse> fetchAllGroups(String search);
    SplitGroupsResponse detailGroup(Long id);
}
