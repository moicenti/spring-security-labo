package com.server.app.dto.finance;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferRequest {

    @NotNull
    private Long fromAccountId;

    @NotNull
    private Long toAccountId;

    @NotNull
    @Positive
    private Double amount;

    private String description;

    private Long categoryId;
}