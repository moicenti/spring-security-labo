package com.server.app.dto.finance;

public record AccountResponse(
        Long id,
        String alias,
        String currency,
        Double baseBalance,
        String type
) {}