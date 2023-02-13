package pl.sii.shopsystem.product.dto;

import lombok.Builder;

@Builder
public record ProductOutputDto(String id, String type, String title, String manufacturer, String price) {
}