package ru.radzivonnikolai.wallet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.radzivonnikolai.wallet.exception.NotFoundWalletException;
import ru.radzivonnikolai.wallet.exception.PaymentRequiredWalletException;

/**
 * @author Nikolai Radzivon
 * @Date 26.10.2024
 */
@ControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<?> handlerNotFoundWalletException(NotFoundWalletException e, WebRequest webRequest) {
        log.info("Ошибка получения кошелька: ", e);

        return ErrorResponse.makeErrorResponse(webRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerPaymentRequiredWalletException(PaymentRequiredWalletException e,
                                                                   WebRequest webRequest) {
        log.info("Ошибка работы с балансом кошелька: ", e);

        return ErrorResponse.makeErrorResponse(webRequest, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerStringToOperationTypeConverterException(HttpMessageNotReadableException e,
                                                                            WebRequest webRequest) {
        log.info("Ошибка конвертации: ", e);

        return ErrorResponse.makeErrorResponse(webRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                    WebRequest webRequest) {
        log.info("Ошибка составления запроса: ", e);

        return ErrorResponse.makeErrorResponse(webRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                                        WebRequest webRequest) {
        log.info("Ошибка составления запроса: ", e);

        return ErrorResponse.makeErrorResponse(webRequest, HttpStatus.BAD_REQUEST);
    }
}
