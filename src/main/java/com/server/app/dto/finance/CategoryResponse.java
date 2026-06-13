package com.server.app.dto.finance;

public record CategoryResponse(
        Long id,
        String name,
        String type,
        Long parentCategoryId
) {}