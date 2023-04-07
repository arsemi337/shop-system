package exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        Integer statusCode,
        String message,
        LocalDateTime errorTime) {
}
