package pl.sii.shopsystem.customer.service.impl;

import pl.sii.shopsystem.customer.exception.CustomerException;

import java.util.UUID;

import static pl.sii.shopsystem.customer.exception.CustomerExceptionMessages.UUID_HAS_INVALID_FORM;


public class CustomerParser {

    UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new CustomerException(UUID_HAS_INVALID_FORM.getMessage() + uuid);
        }
    }
}
