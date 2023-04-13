package order;

import lombok.Builder;

@Builder
public record OrderProductInputDto(
        String productName,
        String quantity
) {
}
