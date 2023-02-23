package pl.sii.shopsystem.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductExceptionMessages {
    UUID_HAS_INVALID_FORM("Passed UUID has invalid form: "),
    NO_PRODUCT_FOUND("No product with entered ID found");
    private final String message;

}