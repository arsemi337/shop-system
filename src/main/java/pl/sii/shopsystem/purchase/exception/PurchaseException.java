package pl.sii.shopsystem.purchase.exception;

import pl.sii.shopsystem.exception.BusinessLogicException;

public class PurchaseException extends BusinessLogicException {
    public PurchaseException(String errorMessage) {
        super(errorMessage);
    }

    public PurchaseException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
