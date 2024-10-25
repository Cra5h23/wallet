
package ru.radzivonnikolai.wallet.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.radzivonnikolai.wallet.dto.WalletRequestDto;
import ru.radzivonnikolai.wallet.dto.WalletResponseDto;
import ru.radzivonnikolai.wallet.exception.NotFoundWalletException;
import ru.radzivonnikolai.wallet.exception.PaymentRequiredWalletException;
import ru.radzivonnikolai.wallet.mapper.WalletMapper;
import ru.radzivonnikolai.wallet.model.Wallet;
import ru.radzivonnikolai.wallet.repository.WalletRepository;
import ru.radzivonnikolai.wallet.service.WalletService;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Nikolay Radzivon
 * @Date 24.10.2024
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository repository;
    private final WalletMapper mapper;

    @Override
    @Transactional
    public String updateBalance(WalletRequestDto dto) {
        var uuid = dto.walletId();
        var wallet = checkWallet(uuid);
        var operationType = dto.operationType();

        switch (operationType) {
            case DEPOSIT -> {
                var v = deposit(wallet, dto.amount());
                save(v);
                return "На кошелёк %s добавлено %s".formatted(wallet.getId(), dto.amount());
            }
            case WITHDRAW -> {
                var v = withdraw(wallet, dto.amount());
                save(v);
                return "С кошелька %s снято %s".formatted(wallet.getId(), dto.amount());
            }
            default -> throw new PaymentRequiredWalletException("Not enough money");// todo изменить
        }
    }

    private void save(Wallet wallet) {
        repository.save(wallet);
    }


    private Wallet checkWallet(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundWalletException("Wallet %s not found".formatted(id)));
    }


    private Wallet deposit(Wallet wallet, BigDecimal amount) {
        var amount1 = wallet.getAmount();
        var sum = amount1.add(amount);
        wallet.setAmount(sum);
        return wallet;
    }

    private Wallet withdraw(Wallet wallet, BigDecimal amount) {
        var amount1 = wallet.getAmount();

        if (amount1.compareTo(amount) > 0) {
            wallet.setAmount(amount1.subtract(amount));
        } else {
            throw new PaymentRequiredWalletException("Not enough money");
        }
        return wallet;
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponseDto getWallet(UUID walletId) {
        Wallet wallet = checkWallet(walletId);

        return mapper.toWalletResponseDto(wallet);
    }

    @Override
    public Object createWallet() {
        Wallet wallet = new Wallet();

        wallet.setAmount(BigDecimal.valueOf(1000));

        save(wallet);
        return wallet;
    }
}
