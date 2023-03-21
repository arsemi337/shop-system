package pl.sii.shopsystem.kafka.service.impl;

import kafka.dto.ProductDto;
import pl.sii.shopsystem.product.model.Product;

public class KafkaProductMapper {

    public ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .creationTime(product.getCreationTime())
                .title(product.getTitle())
                .genre(product.getGenre())
                .publishingHouse(product.getPublishingHouse())
                .price(product.getPrice())
                .isDeleted(product.isDeleted())
                .build();
    }
}
