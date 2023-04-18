package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductExceptionMessages {
    NO_PRODUCT_FOUND("No product with entered ID was found"),
    NO_PRODUCT_FOUND_BY_NAME("No product with entered name was found: "),
    INVALID_GENRE("Entered type is invalid: "),
    PRICE_NOT_A_NUMBER("Entered product price is not a number: "),
    PRICE_IS_NON_POSITIVE("Entered product price is a non positive value: "),
    NUMBER_NOT_A_NUMBER("Entered products number is not a number: "),
    NUMBER_IS_NOT_POSITIVE("Entered products number is a non positive value: "),
    TOO_MUCH_PRODUCTS_TO_BE_ADDED("The total number of products to be added is to large: "),
    PRODUCT_IS_NOT_CONSISTENT("Entered product is not consistent with the already existing product "),
    NOT_ENOUGH_PRODUCTS_TO_REMOVE("Entered products number to be removed is greater than total number of products in a database");
    private final String message;
}
