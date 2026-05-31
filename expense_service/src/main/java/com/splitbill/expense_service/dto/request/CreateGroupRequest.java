package com.splitbill.expense_service.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateGroupRequest {

    @NotBlank(message = "Group name is required")
    private String name;

    @Valid
    @NotEmpty(message = "Participants cannot be empty")
    private List<CreateParticipantRequest> participants;
}