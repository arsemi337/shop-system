package pl.artur.shopsystem.product.dto;

import lombok.Builder;

@Builder
public record PurchaseProductInputDto(
        String productName,
        String quantity
) {
}
