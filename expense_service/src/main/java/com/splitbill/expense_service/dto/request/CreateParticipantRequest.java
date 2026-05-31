package com.splitbill.expense_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateParticipantRequest {

    @NotBlank(message = "Participant name is required")
    private String name;
}
