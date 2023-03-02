package pl.sii.shopsystem.order.orderProduct.dto;

import lombok.Builder;

@Builder
public record OrderProductInputDto(
        String productId,
        String quantity) {
}
