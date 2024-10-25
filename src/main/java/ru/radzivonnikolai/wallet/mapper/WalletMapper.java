package ru.radzivonnikolai.wallet.mapper;

import org.springframework.stereotype.Component;
import ru.radzivonnikolai.wallet.dto.WalletResponseDto;
import ru.radzivonnikolai.wallet.model.Wallet;

/**
 * @author Nikolay Radzivon
 * @Date 24.10.2024
 */
@Component
public class WalletMapper {

    public WalletResponseDto toWalletResponseDto(Wallet wallet) {
        return new WalletResponseDto(wallet.getId(), wallet.getAmount());
    }
}