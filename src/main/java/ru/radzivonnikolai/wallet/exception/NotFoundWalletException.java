package ru.radzivonnikolai.wallet.exception;

/**
 * @author Nikolay Radzivon
 * @Date 24.10.2024
 */
public class NotFoundWalletException extends RuntimeException{
    public NotFoundWalletException(String message) {
        super(message);
    }
}