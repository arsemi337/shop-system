package pl.sii.shopsystem.order.orderProduct.dto;

import lombok.Builder;
import product.model.Genre;

import java.math.BigDecimal;

@Builder
public record OrderProductOutputDto(
        String title,
        String author,
        Genre genre,
        String publishingHouse,
        BigDecimal price,
        int quantity) {
}
