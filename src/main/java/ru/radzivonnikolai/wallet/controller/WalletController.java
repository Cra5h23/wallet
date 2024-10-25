package ru.radzivonnikolai.wallet.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
public class WalletController {
    private final WalletService service;

    @PostMapping("/wallet")
    public ResponseEntity<Object> updateBalance(@RequestBody WalletRequestDto dto) {
        return ResponseEntity
                .ok(service.updateBalance(dto));
    }

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<Object> getWallet(@PathVariable(value = "WALLET_UUID") UUID walletId) {
        return ResponseEntity
                .ok(service.getWallet(walletId));
    }

    @PostMapping()
    public ResponseEntity<Object> createWallet() {
        return ResponseEntity
                .ok(service.createWallet());
    }
}