package pl.artur.shopsystem.product.service.impl;

import org.springframework.stereotype.Component;
import pl.artur.shopsystem.product.service.ProductParser;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static exception.ProductExceptionMessages.*;

@Component
public class ProductParserImpl implements ProductParser {

    @Override
    public int parseAddProductsNumber(String stringNumber) {
        int number;
        try {
            number = Integer.parseInt(stringNumber);
            if (number <= 0) {
                throw new IllegalArgumentException(NUMBER_IS_NOT_POSITIVE.getMessage() + number);
            }
        } catch (NumberFormatException e) {
            number = 1;
        }
        return number;
    }

    @Override
    public int parseProductsNumber(String stringNumber) {
        int number;
        try {
            number = Integer.parseInt(stringNumber);
            if (number <= 0) {
                throw new IllegalArgumentException(NUMBER_IS_NOT_POSITIVE.getMessage() + number);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(NUMBER_NOT_A_NUMBER.getMessage() + stringNumber);
        }
        return number;
    }

    @Override
    public void validateBigDecimal(String stringNumber) {
        try {
            BigDecimal number = new BigDecimal(stringNumber);
            if (number.compareTo(BigDecimal.ONE) < 1) {
                throw new IllegalArgumentException(PRICE_IS_NON_POSITIVE.getMessage() + number);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException(PRICE_NOT_A_NUMBER.getMessage() + stringNumber);
        }
    }

    @Override
    public LocalDate parseLocalDate(String stringDate) {
            try {
                return LocalDate.parse(stringDate);
            } catch (DateTimeParseException e) {
                throw new DateTimeException(INVALID_DATE.getMessage() + stringDate);
            }
    }
}
