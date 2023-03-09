package pl.sii.shopsystem.product.service.impl;

import kafka.dto.ProductDto;
import pl.sii.shopsystem.product.dto.ProductInputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.model.Product;
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
                .type(productInputDto.type())
                .manufacturer(productInputDto.manufacturer())
                .price(new BigDecimal(productInputDto.price()))
                .build();
    }

    ProductOutputDto mapToProductOutputDto(Product product) {
        return ProductOutputDto.builder()
                .id(product.getId().toString())
                .type(product.getType())
                .title(product.getTitle())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .build();
    }

    public ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .creationTime(product.getCreationTime())
                .title(product.getTitle())
                .type(product.getType())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .isDeleted(product.isDeleted())
                .build();
    }
}