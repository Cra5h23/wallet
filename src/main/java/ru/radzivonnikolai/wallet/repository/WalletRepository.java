package ru.radzivonnikolai.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.radzivonnikolai.wallet.model.Wallet;

import java.util.UUID;

/**
 * @author Nikolay Radzivon
 * @Date 24.10.2024
 */
public interface WalletRepository extends JpaRepository<Wallet, UUID> {
}