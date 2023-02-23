package pl.sii.shopsystem.product.service.impl;

import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.model.Product;

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
}