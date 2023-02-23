package pl.sii.shopsystem.order.exception;

import pl.sii.shopsystem.exception.BusinessLogicException;

public class OrderException extends BusinessLogicException {
    public OrderException(String errorMessage) {
        super(errorMessage);
    }

    public OrderException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
