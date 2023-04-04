package pl.artur.shopsystem.product.dto;

import lombok.Builder;

@Builder
public record AddProductInputDto(
        String name,
        String type,
        String manufacturer,
        String price,
        String number) {
}