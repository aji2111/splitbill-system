package com.splitbill.expense_service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class GroupResponse {

    private UUID groupPublicId;

    private String name;

    private List<ParticipantResponse> participants;

    private LocalDateTime createdAt;
}