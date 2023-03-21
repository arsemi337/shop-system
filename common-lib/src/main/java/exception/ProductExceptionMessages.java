package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductExceptionMessages {
    NO_PRODUCT_FOUND("No product with entered ID found"),
    INVALID_GENRE("Entered type is invalid: "),
    PRICE_NOT_A_NUMBER("Entered product price is not a number: "),
    PRICE_IS_NON_POSITIVE("Entered product price is a non positive number: "),
    PRODUCT_ALREADY_EXISTS("Product with entered title already exists: ");
    private final String message;
}
