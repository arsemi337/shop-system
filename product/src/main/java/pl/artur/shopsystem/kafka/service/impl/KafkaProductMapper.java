package pl.artur.shopsystem.kafka.service.impl;

import kafka.dto.ProductDto;
import pl.artur.shopsystem.product.model.Product;

import java.util.Map;

public class KafkaProductMapper {

    public ProductDto mapToProductDto(Map.Entry<Product, Integer> productToProductCountEntry) {
        return ProductDto.builder()
                .name(productToProductCountEntry.getKey().getName())
                .type(productToProductCountEntry.getKey().getType())
                .manufacturer(productToProductCountEntry.getKey().getManufacturer())
                .price(productToProductCountEntry.getKey().getPrice())
                .count(productToProductCountEntry.getValue())
                .build();
    }
}
