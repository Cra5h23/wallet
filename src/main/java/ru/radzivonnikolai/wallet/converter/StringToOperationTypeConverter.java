package ru.radzivonnikolai.wallet.converter;

import org.springframework.core.convert.converter.Converter;
import ru.radzivonnikolai.wallet.dto.OperationType;
import ru.radzivonnikolai.wallet.exception.StringToOperationTypeConverterException;

/**
 * @author Nikolai Radzivon
 * @Date 25.10.2024
 */
public class StringToOperationTypeConverter implements Converter<String, OperationType> {
    @Override
    public OperationType convert(String source) {
        try {
            return OperationType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new StringToOperationTypeConverterException("The operation \"%s\" does not exist".formatted(source));
        }
    }
}
