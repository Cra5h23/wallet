
package ru.radzivonnikolai.wallet.service;

import ru.radzivonnikolai.wallet.dto.WalletRequestDto;
import ru.radzivonnikolai.wallet.dto.WalletResponseDto;

import java.net.URI;
import java.util.UUID;

/**
 * @author Nikolai Radzivon
 * @Date 24.10.2024
 */
public interface WalletService {
    String updateBalance(WalletRequestDto dto);

    WalletResponseDto getWallet(UUID walletId);

    URI createWallet();
}
