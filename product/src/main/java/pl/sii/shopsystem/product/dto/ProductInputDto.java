package pl.sii.shopsystem.product.dto;

import lombok.Builder;

@Builder
public record ProductInputDto(
        String title,
        String type,
        String manufacturer,
        String price) {
}
