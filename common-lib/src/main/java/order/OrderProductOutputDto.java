package order;

import lombok.Builder;

@Builder
public record OrderProductOutputDto(
        String name,
        int quantity
) {
}
