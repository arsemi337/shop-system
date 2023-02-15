package pl.sii.shopsystem.product.service.impl;

import pl.sii.shopsystem.purchase.exception.PurchaseException;

import java.util.UUID;

import static pl.sii.shopsystem.product.exception.ProductExceptionMessages.UUID_HAS_INVALID_FORM;


public class ProductParser {

    UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new PurchaseException(UUID_HAS_INVALID_FORM.getMessage() + uuid);
        }
    }
}
