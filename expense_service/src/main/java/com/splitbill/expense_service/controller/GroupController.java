package com.splitbill.expense_service.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitbill.expense_service.dto.request
        .CreateGroupRequest;

import com.splitbill.expense_service.dto.response
        .BaseResponse;

import com.splitbill.expense_service.dto.response
        .GroupResponse;

import com.splitbill.expense_service.service
        .GroupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/groups")

@RequiredArgsConstructor

@Tag(
        name = "Group API",
        description = "API for split bill groups"
)
public class GroupController {

    private final GroupService
            groupService;

    @Operation(summary = "Create new group")
    @PostMapping
    public ResponseEntity<BaseResponse<GroupResponse>>
    createGroup(

            @Valid
            @RequestBody
            CreateGroupRequest request
    ) {

        GroupResponse groupResponse =
                groupService.createGroup(request);

        BaseResponse<GroupResponse> response =

                BaseResponse.<GroupResponse>builder()

                        .message(
                                "Group created successfully"
                        )

                        .data(groupResponse)

                        .build();

        return ResponseEntity.ok(response);
    }
}

