package pl.artur.shopsystem.product.dto;

import lombok.Builder;
import product.model.Genre;

@Builder
public record ProductTypeNumberOutputDto(
        Genre type,
        long productsNumber
) {
}
