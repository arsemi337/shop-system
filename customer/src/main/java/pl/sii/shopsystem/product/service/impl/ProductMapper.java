package pl.sii.shopsystem.product.service.impl;

import kafka.dto.ProductDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.model.Product;

class ProductMapper {

    ProductOutputDto mapToProductOutputDto(Product product) {
        return ProductOutputDto.builder()
                .id(product.getId().toString())
                .type(product.getType())
                .title(product.getTitle())
                .publishingHouse(product.getPublishingHouse())
                .price(product.getPrice())
                .build();
    }

    public Product mapToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.id())
                .creationTime(productDto.creationTime())
                .title(productDto.title())
                .type(productDto.type())
                .publishingHouse(productDto.publishingHouse())
                .price(productDto.price())
                .isDeleted(productDto.isDeleted())
                .build();
    }
}