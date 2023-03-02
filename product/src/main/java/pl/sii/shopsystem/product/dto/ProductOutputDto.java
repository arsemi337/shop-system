package pl.sii.shopsystem.product.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductOutputDto(
        String id,
        String type,
        String title,
        String manufacturer,
        BigDecimal price) {
}