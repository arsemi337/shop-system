package pl.artur.shopsystem.product.service.impl;

import org.springframework.stereotype.Component;
import pl.artur.shopsystem.product.service.ProductParser;

import static exception.ProductExceptionMessages.NUMBER_IS_NOT_POSITIVE;

@Component
public class ProductParserImpl implements ProductParser {

    @Override
    public int parseNumber(String stringNumber) {
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
}
