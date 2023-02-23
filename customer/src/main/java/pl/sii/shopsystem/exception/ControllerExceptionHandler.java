package pl.sii.shopsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import supplier.TimeSupplier;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final TimeSupplier timeSupplier;

    public ControllerExceptionHandler(TimeSupplier timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    @ExceptionHandler
    ResponseEntity<ErrorResponse> exceptionHandler(RuntimeException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(e.getMessage())
                .errorTime(timeSupplier.get())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
