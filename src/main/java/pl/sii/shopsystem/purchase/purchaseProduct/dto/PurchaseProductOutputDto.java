package pl.sii.shopsystem.purchase.purchaseProduct.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PurchaseProductOutputDto(
        String title,
        String type,
        String manufacturer,
        BigDecimal price,
        int quantity) {
}
