package com.server.app.dto.finance;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotBlank
    private String alias;

    @NotBlank
    private String currency;

    @NotBlank
    private String type;
}