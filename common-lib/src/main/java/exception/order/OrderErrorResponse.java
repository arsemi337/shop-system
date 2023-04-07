package exception.order;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderErrorResponse(
        Integer statusCode,
        String message,
        LocalDateTime errorTime,
        List<ProductErrorDto> productsFailedToBePurchase
) {
}
