package pl.artur.shopsystem.product.service.impl;

import exception.order.ProductErrorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.artur.shopsystem.product.dto.AddProductInputDto;
import pl.artur.shopsystem.product.dto.MassProductOutputDto;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.dto.PurchaseProductInputDto;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import pl.artur.shopsystem.product.service.ProductParser;
import product.model.Genre;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static exception.ProductExceptionMessages.MASS_MAPPING_ERROR;
import static java.util.stream.Collectors.groupingBy;

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

    Map.Entry<String, List<Product>> mapToStringToProductListMap(
            PurchaseProductInputDto purchaseProductInputDto,
            List<ProductErrorDto> errorDtoList) {
        int quantity = parser.parsePurchaseQuantity(purchaseProductInputDto.quantity());
        String productName = purchaseProductInputDto.productName();

        Page<Product> productPage = productRepository.findAllByName(
                productName,
                PageRequest.of(0, quantity));

        List<Product> products = productPage.get().toList();

        if (products.size() < quantity) {
            errorDtoList.add(
                    ProductErrorDto.builder()
                            .productName(productName)
                            .remaining(products.size())
                            .build());
        }

        return Map.entry(purchaseProductInputDto.productName(), products);
    }
}