package pl.sii.shopsystem.purchase.dto;

import lombok.Builder;

@Builder
public record PurchaseInputDto(String userId, String productId) {
}
