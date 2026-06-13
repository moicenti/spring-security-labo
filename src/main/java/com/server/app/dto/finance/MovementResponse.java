package com.server.app.dto.finance;

import java.time.LocalDateTime;

public record MovementResponse(
        Long id,
        Double amount,
        String originalCurrency,
        Double exchangeRate,
        LocalDateTime date,
        String description,
        AccountInfo account,
        CategoryInfo category
) {}