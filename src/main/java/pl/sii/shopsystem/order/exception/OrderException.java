package pl.sii.shopsystem.order.exception;

public class OrderException extends RuntimeException {
    public OrderException(String errorMessage) {
        super(errorMessage);
    }

    public OrderException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
