package pl.sii.shopsystem.client.exception;

public enum ClientExceptionMessages {
    INPUT_DATA_CONTAINS_BLANK_FIELDS("Input data cannot contain blank fields"),
    CLIENT_ALREADY_EXISTS("Client with given email address already exists"),
    EMAIL_ALREADY_USED("Given email address is already used by other client"),

    NO_CLIENT_FOUND("No client with passed email exists"),
    NO_PRODUCT_FOUND("No product with passed id exists");

    private final String message;

    ClientExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
