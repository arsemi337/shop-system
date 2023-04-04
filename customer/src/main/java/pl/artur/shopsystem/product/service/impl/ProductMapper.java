package pl.artur.shopsystem.product.service.impl;

import kafka.dto.ProductDto;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.model.Product;

class ProductMapper {

    ProductOutputDto mapToProductOutputDto(Product product) {
        return ProductOutputDto.builder()
                .id(product.getId().toString())
                .type(product.getType())
                .title(product.getTitle())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .build();
    }

    public Product mapToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.id())
                .creationTime(productDto.creationTime())
                .title(productDto.title())
                .type(productDto.type())
                .manufacturer(productDto.manufacturer())
                .price(productDto.price())
                .isDeleted(productDto.isDeleted())
                .build();
    }
}