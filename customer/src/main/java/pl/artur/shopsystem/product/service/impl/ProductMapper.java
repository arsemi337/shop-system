package pl.artur.shopsystem.product.service.impl;

import kafka.dto.ProductDto;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.model.Product;
import supplier.TimeSupplier;

import java.util.UUID;

class ProductMapper {

    private final TimeSupplier timeSupplier;

    public ProductMapper(TimeSupplier timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    ProductOutputDto mapToProductOutputDto(Product product) {
        return ProductOutputDto.builder()
                .id(product.getId().toString())
                .type(product.getType())
                .name(product.getName())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .build();
    }

    public Product mapToProduct(ProductDto productDto) {
        return Product.builder()
                .id(UUID.randomUUID())
                .creationTime(timeSupplier.get())
                .name(productDto.name())
                .type(productDto.type())
                .manufacturer(productDto.manufacturer())
                .price(productDto.price())
                .isDeleted(false)
                .build();
    }
}