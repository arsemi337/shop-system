package pl.artur.shopsystem.product.service.impl;

import pl.artur.shopsystem.product.dto.AddProductInputDto;
import pl.artur.shopsystem.product.dto.MassProductOutputDto;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.model.Product;
import product.model.Genre;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static exception.ProductExceptionMessages.MASS_MAPPING_ERROR;
import static java.util.stream.Collectors.groupingBy;

class ProductMapper {

    private final TimeSupplier timeSupplier;

    public ProductMapper(TimeSupplier timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    Product mapToProduct(AddProductInputDto addProductInputDto) {
        return Product.builder()
                .creationTime(timeSupplier.get())
                .name(addProductInputDto.name())
                .type(Genre.valueOf(addProductInputDto.type()))
                .manufacturer(addProductInputDto.manufacturer())
                .price(new BigDecimal(addProductInputDto.price()))
                .build();
    }

    ProductOutputDto mapToProductOutputDto(Product product) {
        return ProductOutputDto.builder()
                .id(product.getId().toString())
                .name(product.getName())
                .type(product.getType())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .build();
    }

    // This method needs to receive a list of equal products as a parameter
    MassProductOutputDto mapToMassProductOutputDto(List<Product> products) {
        Map<String, List<Product>> productsPerName = products.stream()
                .collect(groupingBy(Product::getName));
        if (productsPerName.size() != 1) {
            throw new IllegalArgumentException(MASS_MAPPING_ERROR.getMessage());
        }

        Product product = products.get(0);

        return MassProductOutputDto.builder()
                .name(product.getName())
                .type(product.getType())
                .manufacturer(product.getManufacturer())
                .price(product.getPrice())
                .number(products.size())
                .build();
    }
}