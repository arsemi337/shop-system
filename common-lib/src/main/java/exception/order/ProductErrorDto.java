package exception.order;

import lombok.Builder;

@Builder
public record ProductErrorDto(
        String productName,
        int remaining
) {
}
