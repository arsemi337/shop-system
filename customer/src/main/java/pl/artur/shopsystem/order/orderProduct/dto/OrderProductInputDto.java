package pl.artur.shopsystem.order.orderProduct.dto;

import lombok.Builder;

@Builder
public record OrderProductInputDto(
        String productName,
        String quantity) {
}
