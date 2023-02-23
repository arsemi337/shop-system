package pl.sii.shopsystem.product.exception;

public class ProductException  extends RuntimeException {
    public ProductException(String errorMessage) {
        super(errorMessage);
    }
    public ProductException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
