package ru.radzivonnikolai.wallet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Nikolai Radzivon
 * @Date 24.10.2024
 * DTO for {@link ru.radzivonnikolai.wallet.model.Wallet}
 */
public record WalletRequestDto(@NotNull(message = "id cannot be null") UUID walletId,
                               @NotNull(message = "Operation cannot be null") OperationType operationType,
                               @Positive(message = "amount cannot be negative") @NotNull(message = "amount cannot be null")
                               BigDecimal amount) {
}