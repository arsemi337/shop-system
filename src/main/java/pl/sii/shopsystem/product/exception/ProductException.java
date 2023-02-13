package pl.sii.shopsystem.product.exception;

import pl.sii.shopsystem.exception.BusinessLogicException;

public class ProductException  extends BusinessLogicException {
    public ProductException(String errorMessage) {
        super(errorMessage);
    }

    public ProductException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
