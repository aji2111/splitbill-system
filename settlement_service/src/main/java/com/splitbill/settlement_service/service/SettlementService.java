package com.splitbill.settlement_service.service;

import com.splitbill.settlement_service.dto.response.SettlementResponse;

import java.util.UUID;

public interface SettlementService {

    SettlementResponse generateSettlement(
            UUID groupPublicId
    );
}