package pl.sii.shopsystem.product.dto;

import lombok.Builder;
import product.model.Genre;

@Builder
public record ProductInputDto(
        String title,
        String genre,
        String publishingHouse,
        String price) {
}
