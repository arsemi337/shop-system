package pl.sii.shopsystem.order.service.impl;

import static exception.OrderExceptionMessages.PRODUCT_QUANTITY_IS_NOT_NUMBER;

public class OrderParser {

    int parseQuantity(String quantity) {
        int quantityNumber;
        try {
            quantityNumber = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(PRODUCT_QUANTITY_IS_NOT_NUMBER.getMessage() + quantity);
        }
        return quantityNumber;
    }
}
