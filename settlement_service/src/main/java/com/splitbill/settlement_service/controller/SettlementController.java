package com.splitbill.settlement_service.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.splitbill.settlement_service.dto.response.SettlementResponse;
import com.splitbill.settlement_service.service.SettlementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/settlements")
@RequiredArgsConstructor
@Tag(
        name = "Settlement",
        description = "Settlement calculation APIs"
)
@SecurityRequirement(name = "bearerAuth")
public class SettlementController {

    private final SettlementService settlementService;

    @Operation(
            summary = "Generate settlement",
            description = """
                    Generate optimized payment recommendations
                    between participants based on balances.
                    """
    )
     @GetMapping("/{groupPublicId}")
    public SettlementResponse getSettlement(
            @PathVariable UUID groupPublicId
    ) {

        return settlementService.generateSettlement(
                groupPublicId
        );
    }
}