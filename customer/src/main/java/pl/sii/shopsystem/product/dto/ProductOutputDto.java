package pl.sii.shopsystem.product.dto;

import lombok.Builder;
import product.model.Genre;

import java.math.BigDecimal;

@Builder
public record ProductOutputDto(
        String id,
        String title,
        Genre genre,
        String publishingHouse,
        BigDecimal price) {
}