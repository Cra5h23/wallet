
package ru.radzivonnikolai.wallet.service;

import ru.radzivonnikolai.wallet.dto.WalletRequestDto;
import ru.radzivonnikolai.wallet.dto.WalletResponseDto;

import java.util.UUID;

/**
 * @author Nikolay Radzivon
 * @Date 24.10.2024
 */
public interface WalletService {
    String updateBalance(WalletRequestDto dto);

    WalletResponseDto getWallet(UUID walletId);

    Object createWallet(); //Todo
}
