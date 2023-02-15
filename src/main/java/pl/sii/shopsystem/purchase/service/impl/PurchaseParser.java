package pl.sii.shopsystem.purchase.service.impl;

import pl.sii.shopsystem.purchase.exception.PurchaseException;

import java.util.UUID;

import static pl.sii.shopsystem.purchase.exception.PurchaseExceptionMessages.UUID_HAS_INVALID_FORM;
import static pl.sii.shopsystem.purchase.exception.PurchaseExceptionMessages.PRODUCT_QUANTITY_IS_NOT_NUMBER;

public class PurchaseParser {

    UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new PurchaseException(UUID_HAS_INVALID_FORM.getMessage() + uuid);
        }
    }

    int parseQuantity(String quantity) {
        int quantityNumber;
        try {
            quantityNumber = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new PurchaseException(PRODUCT_QUANTITY_IS_NOT_NUMBER.getMessage() + quantity);
        }
        return quantityNumber;
    }
}
