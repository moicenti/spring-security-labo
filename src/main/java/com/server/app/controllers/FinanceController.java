package com.server.app.controllers;

import com.server.app.dto.finance.AccountResponse;
import com.server.app.dto.finance.CategoryResponse;
import com.server.app.dto.finance.CreateAccountRequest;
import com.server.app.dto.finance.MovementResponse;
import com.server.app.dto.finance.TransferRequest;
import com.server.app.dto.response.Pagination;
import com.server.app.services.FinanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/finanzas")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/cuentas")
    public Pagination<AccountResponse> getAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return financeService.getAccounts(page, size);
    }


    @PostMapping("/cuentas")
    public AccountResponse createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        return financeService.createAccount(request);
    }


    @GetMapping("/movimientos")
    public Pagination<MovementResponse> getMovements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {

        return financeService.getMovements(
                page,
                size,
                from,
                to
        );
    }


    @PostMapping("/transferencias")
    public MovementResponse transfer(
            @Valid @RequestBody TransferRequest request
    ) {
        return financeService.transfer(request);
    }


    @GetMapping("/categorias")
    public Pagination<CategoryResponse> getCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return financeService.getCategories(page, size);
    }
}