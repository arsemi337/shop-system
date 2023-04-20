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
    NOT_ENOUGH_PRODUCTS_TO_REMOVE("Entered products number to be removed is greater than total number of products in a database"),
    ILLEGAL_FILTERING_PARAMETER("Entered filtering attribute is not allowed: "),
    FILTERING_PARAMETERS_NOT_COMPLETE("Not all filtering parameters were passed"),
    ILLEGAL_STRING_FILTERING_OPERATION("'=' is the only valid operation in terms of the following attributes: "),
    ILLEGAL_PRICE_FILTERING_OPERATION("'=', '<', '>' are the only valid operations in terms of the following attributes: "),
    ILLEGAL_TIME_FILTERING_OPERATION("'<', '>' are the only valid operations in terms of the following attributes: "),
    INVALID_DATE("Entered date does not match proper format (YYYY-MM-DD): ");
    private final String message;
}
