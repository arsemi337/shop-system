package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductExceptionMessages {
    NO_PRODUCT_FOUND("No product with entered ID found");
    private final String message;
}
