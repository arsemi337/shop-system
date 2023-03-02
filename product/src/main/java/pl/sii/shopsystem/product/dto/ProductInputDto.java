package pl.sii.shopsystem.product.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductInputDto(
        String type,
        String title,
        String manufacturer,
        BigDecimal price) {
}
