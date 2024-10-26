package ru.radzivonnikolai.wallet.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Nikolay Radzivon
 * @Date 24.10.2024
 * DTO for {@link ru.radzivonnikolai.wallet.model.Wallet}
 */
public record WalletResponseDto(UUID walletId, BigDecimal amount) {
}