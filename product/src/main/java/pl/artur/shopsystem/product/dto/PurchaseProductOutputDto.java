package pl.artur.shopsystem.product.dto;

import lombok.Builder;

@Builder
public record PurchaseProductOutputDto(
        String name,
        int quantity
) {
}
