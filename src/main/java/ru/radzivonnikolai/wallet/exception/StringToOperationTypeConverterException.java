package ru.radzivonnikolai.wallet.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Nikolai Radzivon
 * @Date 25.10.2024
 */
public class StringToOperationTypeConverterException extends RuntimeException {
    public StringToOperationTypeConverterException(String message) {
        super(message);
    }
}
