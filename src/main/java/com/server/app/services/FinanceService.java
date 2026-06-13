package com.server.app.services;

import com.server.app.dto.finance.*;
import com.server.app.dto.response.Pagination;
import com.server.app.dto.response.PaginationMeta;
import com.server.app.entities.Account;
import com.server.app.entities.Category;
import com.server.app.entities.Movement;
import com.server.app.entities.User;
import com.server.app.entities.enums.AccountType;
import com.server.app.exceptions.ConfictException;
import com.server.app.exceptions.NotFoundException;
import com.server.app.mapper.FinanceMapper;
import com.server.app.repositories.AccountRepository;
import com.server.app.repositories.CategoryRepository;
import com.server.app.repositories.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final MovementRepository movementRepository;
    private final FinanceMapper mapper;

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest dto) {

        User user = getCurrentUser();

        Account account = Account.builder()
                .alias(dto.getAlias())
                .currency(dto.getCurrency())
                .baseBalance(0.0)
                .type(AccountType.valueOf(dto.getType().toUpperCase()))
                .user(user)
                .build();

        return mapper.toAccountResponse(
                accountRepository.save(account)
        );
    }

    public Pagination<AccountResponse> getAccounts(
            int page,
            int size
    ) {

        User user = getCurrentUser();

        Page<AccountResponse> result =
                accountRepository
                        .findByUser(
                                user,
                                PageRequest.of(page, size)
                        )
                        .map(mapper::toAccountResponse);

        return new Pagination<>(
                result.getContent(),
                new PaginationMeta(
                        result.getNumber(),
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }

    public Pagination<CategoryResponse> getCategories(
            int page,
            int size
    ) {

        Page<CategoryResponse> result =
                categoryRepository
                        .findAll(PageRequest.of(page, size))
                        .map(mapper::toCategoryResponse);

        return new Pagination<>(
                result.getContent(),
                new PaginationMeta(
                        result.getNumber(),
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }

    public Pagination<MovementResponse> getMovements(
            int page,
            int size,
            LocalDate from,
            LocalDate to
    ) {

        LocalDateTime start =
                from != null
                        ? from.atStartOfDay()
                        : LocalDateTime.MIN;

        LocalDateTime end =
                to != null
                        ? to.atTime(23,59,59)
                        : LocalDateTime.MAX;

        Page<MovementResponse> result =
                movementRepository
                        .findByDateBetween(
                                start,
                                end,
                                PageRequest.of(page, size)
                        )
                        .map(mapper::toMovementResponse);

        return new Pagination<>(
                result.getContent(),
                new PaginationMeta(
                        result.getNumber(),
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }

    @Transactional
    public MovementResponse transfer(
            TransferRequest dto
    ) {

        Account source =
                accountRepository.findById(dto.getFromAccountId())
                        .orElseThrow(() ->
                                new NotFoundException("Source account not found"));

        Account destination =
                accountRepository.findById(dto.getToAccountId())
                        .orElseThrow(() ->
                                new NotFoundException("Destination account not found"));

        if (source.getBaseBalance() < dto.getAmount()) {
            throw new ConfictException("Insufficient funds");
        }

        source.setBaseBalance(
                source.getBaseBalance() - dto.getAmount()
        );

        destination.setBaseBalance(
                destination.getBaseBalance() + dto.getAmount()
        );

        accountRepository.save(source);
        accountRepository.save(destination);

        Category category = null;

        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() ->
                            new NotFoundException("Category not found"));
        }

        Movement movement = Movement.builder()
                .amount(dto.getAmount())
                .originalCurrency(source.getCurrency())
                .exchangeRate(1.0)
                .date(LocalDateTime.now())
                .description(dto.getDescription())
                .account(source)
                .category(category)
                .build();

        return mapper.toMovementResponse(
                movementRepository.save(movement)
        );
    }
}