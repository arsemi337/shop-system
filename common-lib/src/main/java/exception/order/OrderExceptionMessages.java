package exception.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderExceptionMessages {
    NOT_ALL_PRODUCTS_AVAILABLE("Not all products are available in the requested quantity"),
    PRODUCT_NOT_FOUND("Product with given ID was not found: "),
    PRODUCT_QUANTITY_IS_NOT_NUMBER("Product quantity is not number: "),
    PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO("Product quantity is not a positive number: ");
    private final String message;
}
