package com.splitbill.group_service.service;

import com.splitbill.common.dto.request.CreateSplitGroupsRequest;
import com.splitbill.common.dto.response.SplitGroupsResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BillGroupsService {
    SplitGroupsResponse createGroup(@Valid @RequestBody CreateSplitGroupsRequest request);
    List<SplitGroupsResponse> fetchAllGroups(String search);
    SplitGroupsResponse detailGroup(Long id);
}
