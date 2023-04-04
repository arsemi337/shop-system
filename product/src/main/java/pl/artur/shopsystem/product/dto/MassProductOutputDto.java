package pl.artur.shopsystem.product.dto;

import lombok.Builder;
import product.model.Genre;

import java.math.BigDecimal;

@Builder
public record MassProductOutputDto(
        String name,
        Genre type,
        String manufacturer,
        BigDecimal price,
        int number
) {
}
