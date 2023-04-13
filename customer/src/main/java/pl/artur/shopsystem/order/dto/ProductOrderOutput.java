package pl.artur.shopsystem.order.dto;

import lombok.Builder;
import product.model.Genre;

@Builder
public record ProductOrderOutput(
        String name,
        Genre type,
        String manufacturer,
        long number
) {
}
