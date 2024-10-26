
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @author Nikolai Radzivon
 * @Date 24.10.2024
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository repository;
    private final WalletMapper mapper;

    /**
     * Метод для обновления баланса кошелька.
     * Если входные данные некорректны (walletId, amount или operationType равны null),
     * метод выбрасывает NotFoundWalletException с сообщением "Not found wallet".
     *
     * @param dto объект DTO с данными для обновления баланса
     * @return строка с информацией о результате операции
     */
    @Override
    @Transactional
    public String updateBalance(WalletRequestDto dto) {
        if (dto == null || dto.walletId() == null || dto.amount() == null || dto.operationType() == null) {
            throw new NotFoundWalletException("Not found wallet");
        }

        log.info("Получен запрос на обновление баланса кошелька {}", dto);
        var walletId = dto.walletId();
        var wallet = checkWallet(walletId);
        var operationType = dto.operationType();

        switch (operationType) {
            case DEPOSIT -> {
                var w = deposit(wallet, dto.amount());

                save(w);
                return "На баланс кошелька с id: %s добавлено %s. Текущий баланс %s"
                        .formatted(wallet.getId(), dto.amount(), w.getAmount());
            }
            case WITHDRAW -> {
                var w = withdraw(wallet, dto.amount());

                save(w);
                return "С баланса кошелька с id: %s снято %s. Текущий баланс %s"
                        .formatted(wallet.getId(), dto.amount(), w.getAmount());
            }
            default -> throw new PaymentRequiredWalletException("Такой операции не существует");
        }
    }

    /**
     * Метод для сохранения кошелька в репозитории.
     *
     * @param wallet кошелек для сохранения
     * @return сохраненный кошелек
     */
    private Wallet save(Wallet wallet) {
        return repository.save(wallet);
    }

    /**
     * Метод для проверки существования кошелька по id.
     * Если кошелек не найден, метод выбрасывает NotFoundWalletException.
     *
     * @param id id кошелька
     * @return кошелек по id
     */
    private Wallet checkWallet(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundWalletException("Кошелёк с id: %s не найден".formatted(id)));
    }

    /**
     * Метод для пополнения баланса кошелька.
     *
     * @param wallet кошелек для пополнения
     * @param amount сумма пополнения
     * @return кошелек с обновленным балансом
     */
    private Wallet deposit(Wallet wallet, BigDecimal amount) {
        var walletAmount = wallet.getAmount();
        var sum = walletAmount.add(amount);

        wallet.setAmount(sum);
        return wallet;
    }

    /**
     * Метод для списания со счета кошелька.
     * Если на балансе кошелька недостаточно средств, метод выбрасывает PaymentRequiredWalletException.
     *
     * @param wallet кошелек для списания
     * @param amount сумма списания
     * @return кошелек с обновленным балансом
     */
    private Wallet withdraw(Wallet wallet, BigDecimal amount) {
        var walletAmount = wallet.getAmount();

        if (walletAmount.compareTo(amount) >= 0) {
            wallet.setAmount(walletAmount.subtract(amount));
        } else {
            throw new PaymentRequiredWalletException("На балансе кошелька с id: %s не хватает денег"
                    .formatted(wallet.getId()));
        }
        return wallet;
    }

    /**
     * Метод для получения кошелька по id.
     *
     * @param walletId id кошелька
     * @return DTO с данными кошелька
     */
    @Override
    @Transactional(readOnly = true)
    public WalletResponseDto getWallet(UUID walletId) {
        log.info("Получен запрос на получение баланса кошелька {}", walletId);
        var wallet = checkWallet(walletId);

        return mapper.toWalletResponseDto(wallet);
    }

    /**
     * Метод для создания нового кошелька.
     *
     * @return URI нового кошелька
     */
    @Override
    @Transactional
    public URI createWallet() {
        var wallet = save(new Wallet());

        try {
            var uri = new URI("http://localhost:8080/api/v1/wallets/%s".formatted(wallet.getId()));
            log.info("Создан новый кошелёк с id: {} и ссылкой {}", wallet.getId(), uri);

            return uri;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
