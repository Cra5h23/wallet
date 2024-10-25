package ru.radzivonnikolai.wallet.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Nikolay Radzivon
 * @Date 24.10.2024
 * DTO for {@link ru.radzivonnikolai.wallet.model.Wallet}
 */
public record WalletRequestDto(@NotNull(message = "id cannot be null") UUID walletId,
                               @NotNull(message = "Operation cannot be null") OperationType operationType,
                               @Positive(message = "amount cannot be negative") BigDecimal amount) {
}