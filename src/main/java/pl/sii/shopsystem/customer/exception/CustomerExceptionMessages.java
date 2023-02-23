package pl.sii.shopsystem.customer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CustomerExceptionMessages {
    INPUT_DATA_CONTAINS_BLANK_FIELDS("Input data cannot contain blank fields"),
    CUSTOMER_ALREADY_EXISTS("Customer with given email address already exists"),
    EMAIL_ALREADY_USED("Given new email address is already used by other customer"),
    NO_CUSTOMER_BY_ID_FOUND("No customer with passed id exists"),
    NO_CUSTOMER_BY_EMAIL_FOUND("No customer with passed email exists");

    private final String message;
}