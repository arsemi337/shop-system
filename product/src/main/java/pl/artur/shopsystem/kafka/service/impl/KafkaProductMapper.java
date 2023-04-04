package pl.artur.shopsystem.kafka.service.impl;

import kafka.dto.ProductDto;
import pl.artur.shopsystem.product.model.Product;

public class KafkaProductMapper {

    public ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .type(product.getType())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .build();
    }
}
