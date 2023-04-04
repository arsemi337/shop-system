package pl.artur.shopsystem.product.dto;

import lombok.Builder;

@Builder
public record RemoveProductInputDto(
        String name
) {
}
