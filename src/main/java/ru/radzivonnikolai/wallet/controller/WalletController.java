package ru.radzivonnikolai.wallet.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.radzivonnikolai.wallet.dto.WalletRequestDto;
import ru.radzivonnikolai.wallet.service.WalletService;

import java.util.UUID;

/**
 * @author Nikolai Radzivon
 * @Date 24.10.2024
 */
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class WalletController {
    private final WalletService service;

    /**
     * Метод для обновления баланса кошелька.
     *
     * @param dto объект DTO с данными для обновления баланса
     * @return ответ с кодом состояния HTTP и телом ответа
     */
    @PostMapping("/wallet")
    public ResponseEntity<Object> updateBalance(@Valid @RequestBody WalletRequestDto dto) {
        log.info("Получен запрос: POST /api/v1/wallet body={}", dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.updateBalance(dto));
    }

    /**
     * Метод для получения кошелька по id.
     *
     * @param walletId id кошелька
     * @return ответ с кодом состояния HTTP и телом ответа
     */
    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<Object> getWallet(@PathVariable(value = "WALLET_UUID") @NotNull UUID walletId) {
        log.info("Получен запрос: GET /api/v1/wallets/{}", walletId);

        return ResponseEntity
                .ok(service.getWallet(walletId));
    }

    /**
     * Метод для создания нового кошелька.
     *
     * @return ответ с кодом состояния HTTP и телом ответа
     */
    @PostMapping("/new-wallet")
    public ResponseEntity<Object> createWallet() {
        log.info("Получен запрос: POST /api/v1");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createWallet());
    }
}