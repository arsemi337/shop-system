package pl.artur.shopsystem.product.service;

import java.time.LocalDate;

public interface ProductParser {

    /**
     * Parses number of products
     * @param stringNumber String representing products number
     * @return int number parsed from the input. In case the passed value is null or is not a number,
     * a default value of 1 is returned
     * @throws IllegalArgumentException if parsed products number is not positive
     */
    int parseAddProductsNumber(String stringNumber);

    /**
     * Parses number of products
     * @param stringNumber String representing products number
     * @return int number parsed from the input
     * @throws IllegalArgumentException if parsed products number is not a number or is not positive
     */
    int parseProductsNumber(String stringNumber);

    /**
     * Validates entered numbers can be parsed to BigDecimal value
     * @param stringNumber
     * @throws IllegalArgumentException if passed value cannot be parsed to BigDecimal
     */
    void validateBigDecimal(String stringNumber);
    LocalDate parseLocalDate(String stringDate);
}
