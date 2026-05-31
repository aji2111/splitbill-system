package com.splitbill.expense_service.service;

import com.splitbill.expense_service.dto.request.CreateGroupRequest;
import com.splitbill.expense_service.dto.response.GroupResponse;

public interface GroupService {

    public GroupResponse createGroup(CreateGroupRequest request);
}


