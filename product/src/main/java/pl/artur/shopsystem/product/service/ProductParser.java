package pl.artur.shopsystem.product.service;

public interface ProductParser {

    /**
     * Parse number of products
     * @param stringNumber String representing products number
     * @return int number parsed from the input. In case the passed value is null or is not a number,
     * a default value of 1 is returned
     * @throws IllegalArgumentException if parsed products number is not positive
     */
    int parseAddProductsNumber(String stringNumber);

    /**
     * Parse number of products
     * @param stringNumber String representing products number
     * @return int number parsed from the input
     * @throws IllegalArgumentException if parsed products number is not a number or is not positive
     */
    int parseProductsNumber(String stringNumber);
}
