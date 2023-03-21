package pl.sii.shopsystem.order.orderProduct.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderProductOutputDto(
        String title,
        String type,
        String publishingHouse,
        BigDecimal price,
        int quantity) {
}
