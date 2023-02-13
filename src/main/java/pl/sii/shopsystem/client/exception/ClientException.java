package pl.sii.shopsystem.client.exception;

import pl.sii.shopsystem.exception.BusinessLogicException;

public class ClientException extends BusinessLogicException {
    public ClientException(String errorMessage) {
        super(errorMessage);
    }

    public ClientException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
