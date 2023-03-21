package pl.sii.shopsystem.product.service.impl;

import pl.sii.shopsystem.product.dto.ProductInputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.model.Product;
import product.model.Genre;
import supplier.TimeSupplier;

import java.math.BigDecimal;

class ProductMapper {

    private final TimeSupplier timeSupplier;

    public ProductMapper(TimeSupplier timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    Product mapToProduct(ProductInputDto productInputDto) {
        return Product.builder()
                .creationTime(timeSupplier.get())
                .title(productInputDto.title())
                .genre(Genre.valueOf(productInputDto.genre()))
                .publishingHouse(productInputDto.publishingHouse())
                .price(new BigDecimal(productInputDto.price()))
                .build();
    }

    ProductOutputDto mapToProductOutputDto(Product product) {
        return ProductOutputDto.builder()
                .id(product.getId().toString())
                .genre(product.getGenre())
                .title(product.getTitle())
                .publishingHouse(product.getPublishingHouse())
                .price(product.getPrice())
                .build();
    }
}