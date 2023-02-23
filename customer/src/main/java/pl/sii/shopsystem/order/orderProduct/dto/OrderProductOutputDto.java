package pl.sii.shopsystem.order.orderProduct.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderProductOutputDto(
        String title,
        String type,
        String manufacturer,
        BigDecimal price,
        int quantity) {
}
