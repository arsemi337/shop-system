package pl.sii.shopsystem.customer.exception;

import pl.sii.shopsystem.exception.BusinessLogicException;

public class CustomerException extends BusinessLogicException {
    public CustomerException(String errorMessage) {
        super(errorMessage);
    }

    public CustomerException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
