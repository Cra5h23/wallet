package ru.radzivonnikolai.wallet.exception;

/**
 * @author Nikolay Radzivon
 * @Date 24.10.2024
 */
public class PaymentRequiredWalletException extends RuntimeException {
    public PaymentRequiredWalletException(String message) {
        super(message);
    }
}