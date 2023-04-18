package pl.artur.shopsystem.product.service.impl;

import exception.order.ProductErrorDto;
import order.OrderProductInputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.artur.shopsystem.product.dto.AddProductInputDto;
import pl.artur.shopsystem.product.dto.MassProductOutputDto;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import pl.artur.shopsystem.product.service.ProductParser;
import product.model.Genre;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

class ProductMapper {

    private final TimeSupplier timeSupplier;
    private final ProductRepository productRepository;
    private final ProductParser parser;

    public ProductMapper(TimeSupplier timeSupplier, ProductRepository productRepository, ProductParser parser) {
        this.timeSupplier = timeSupplier;
        this.productRepository = productRepository;
        this.parser = parser;
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

    MassProductOutputDto mapToMassProductOutputDto(Map.Entry<Product, Integer> productToProductCountEntry) {
        return MassProductOutputDto.builder()
                .name(productToProductCountEntry.getKey().getName())
                .type(productToProductCountEntry.getKey().getType())
                .manufacturer(productToProductCountEntry.getKey().getManufacturer())
                .price(productToProductCountEntry.getKey().getPrice())
                .number(productToProductCountEntry.getValue())
                .build();
    }

    Map.Entry<String, List<Product>> mapToStringToProductListMap(
            OrderProductInputDto orderProductInputDto,
            List<ProductErrorDto> errorDtoList) {
        int quantity = parser.parseProductsNumber(orderProductInputDto.quantity());
        String productName = orderProductInputDto.productName();

        Page<Product> productPage = productRepository.findAllByNameAndIsDeleted(
                productName,
                false,
                PageRequest.of(0, quantity));

        List<Product> products = productPage.get().toList();

        if (products.size() < quantity) {
            errorDtoList.add(
                    ProductErrorDto.builder()
                            .productName(productName)
                            .remaining(products.size())
                            .build());
        }

        return Map.entry(orderProductInputDto.productName(), products);
    }
}