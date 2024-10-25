package ru.radzivonnikolai.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.radzivonnikolai.wallet.converter.StringToOperationTypeConverter;

/**
 * @author Nikolai Radzivon
 * @Date 25.10.2024
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public StringToOperationTypeConverter stringToOperationTypeConverter() {
        return new StringToOperationTypeConverter();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToOperationTypeConverter());
    }
}
