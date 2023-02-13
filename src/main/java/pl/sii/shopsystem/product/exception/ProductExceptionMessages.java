package pl.sii.shopsystem.product.exception;

public enum ProductExceptionMessages {
    NO_PRODUCT_FOUND("No product with entered ID found");

    private final String message;

    ProductExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}