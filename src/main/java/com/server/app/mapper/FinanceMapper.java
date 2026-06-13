package com.server.app.mapper;

import com.server.app.dto.finance.*;
import com.server.app.entities.Account;
import com.server.app.entities.Category;
import com.server.app.entities.Movement;
import org.springframework.stereotype.Component;

@Component
public class FinanceMapper {

    public AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAlias(),
                account.getCurrency(),
                account.getBaseBalance(),
                account.getType().name()
        );
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType().name(),
                category.getParentCategory() != null
                        ? category.getParentCategory().getId()
                        : null
        );
    }

    public MovementResponse toMovementResponse(Movement movement) {

        AccountInfo account =
                new AccountInfo(
                        movement.getAccount().getId(),
                        movement.getAccount().getAlias()
                );

        CategoryInfo category =
                movement.getCategory() != null
                        ? new CategoryInfo(
                        movement.getCategory().getId(),
                        movement.getCategory().getName(),
                        movement.getCategory().getType().name()
                )
                        : null;

        return new MovementResponse(
                movement.getId(),
                movement.getAmount(),
                movement.getOriginalCurrency(),
                movement.getExchangeRate(),
                movement.getDate(),
                movement.getDescription(),
                account,
                category
        );
    }
}