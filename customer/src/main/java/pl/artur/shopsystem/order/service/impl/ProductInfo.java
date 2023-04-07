package pl.artur.shopsystem.order.service.impl;

import lombok.Builder;
import product.model.Genre;

import java.math.BigDecimal;

@Builder
public record ProductInfo(
        String name,
        Genre type,
        String manufacturer,
        BigDecimal price) {
}
