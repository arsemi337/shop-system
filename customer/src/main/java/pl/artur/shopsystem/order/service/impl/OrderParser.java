package pl.artur.shopsystem.order.service.impl;

import static exception.ProductExceptionMessages.NUMBER_IS_NOT_POSITIVE;
import static exception.ProductExceptionMessages.NUMBER_NOT_A_NUMBER;

public class OrderParser {

    int parseQuantity(String stringQuantity) {
        int quantity;
        try {
            quantity = Integer.parseInt(stringQuantity);
            if (quantity <= 0) {
                throw new IllegalArgumentException(NUMBER_IS_NOT_POSITIVE.getMessage() + quantity);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException(NUMBER_NOT_A_NUMBER.getMessage() + stringQuantity);
        }
        return quantity;
    }
}
