package pl.artur.shopsystem.product.dto;

import lombok.Builder;

@Builder
public record AlterProductInputDto(
        String name,
        String type,
        String manufacturer,
        String price) {
}
