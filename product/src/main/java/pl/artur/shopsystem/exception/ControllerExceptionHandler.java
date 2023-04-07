package pl.artur.shopsystem.exception;

import exception.ErrorResponse;
import exception.order.OrderErrorResponse;
import exception.order.OrderException;
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
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler
    ResponseEntity<OrderErrorResponse> purchaseExceptionHandler(OrderException e) {
        OrderErrorResponse errorResponse = OrderErrorResponse.builder()
                .statusCode(BAD_REQUEST.value())
                .message(e.getMessage())
                .errorTime(timeSupplier.get())
                .productsFailedToBePurchase(e.getErrorDtoList())
                .build();
        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }
}
