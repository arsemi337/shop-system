package pl.sii.shopsystem.product.exception;

public enum ProductExceptionMessages {
    UUID_HAS_INVALID_FORM("Passed UUID has invalid form: "),
    NO_PRODUCT_FOUND("No product with entered ID found");
    private final String message;

    ProductExceptionMessages(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}