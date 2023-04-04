package pl.artur.shopsystem.order.orderProduct.dto;

import lombok.Builder;
import product.model.Genre;

import java.math.BigDecimal;

@Builder
public record OrderProductOutputDto(
        String title,
        Genre type,
        String manufacturer,
        BigDecimal price,
        int quantity) {
}
