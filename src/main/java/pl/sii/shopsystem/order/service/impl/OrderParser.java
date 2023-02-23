package pl.sii.shopsystem.order.service.impl;

import pl.sii.shopsystem.order.exception.OrderException;

import java.util.UUID;

import static pl.sii.shopsystem.order.exception.OrderExceptionMessages.UUID_HAS_INVALID_FORM;
import static pl.sii.shopsystem.order.exception.OrderExceptionMessages.PRODUCT_QUANTITY_IS_NOT_NUMBER;

public class OrderParser {

    UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new OrderException(UUID_HAS_INVALID_FORM.getMessage() + uuid);
        }
    }

    int parseQuantity(String quantity) {
        int quantityNumber;
        try {
            quantityNumber = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new OrderException(PRODUCT_QUANTITY_IS_NOT_NUMBER.getMessage() + quantity);
        }
        return quantityNumber;
    }
}
