package pl.artur.shopsystem.product.service.impl;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.artur.shopsystem.product.dto.AddProductInputDto;
import pl.artur.shopsystem.product.dto.AlterProductInputDto;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import pl.artur.shopsystem.product.service.ProductParser;
import pl.artur.shopsystem.product.service.ProductValidator;
import product.model.Genre;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static exception.CustomerExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static exception.ProductExceptionMessages.*;

@Component
public class ProductValidatorImpl implements ProductValidator {

    private final ProductRepository productRepository;
    private final ProductParser parser;

    public ProductValidatorImpl(ProductRepository productRepository, ProductParser parser) {
        this.productRepository = productRepository;
        this.parser = parser;
    }

    @Override
    public void validateNumberOfProductsToBeAdded(List<AddProductInputDto> addProductInputDtoList) {
        int totalNumber = addProductInputDtoList.stream()
                .map(productInputDto -> parser.parseNumber(productInputDto.number()))
                .mapToInt(Integer::intValue)
                .sum();
        if (totalNumber > 10_000) {
            throw new IllegalArgumentException(TOO_MUCH_PRODUCTS_TO_BE_ADDED.getMessage() + totalNumber);
        }
    }

    @Override
    public void validateAddProductInputDto(AddProductInputDto addProductInputDto) {
        if (isAnyBlank(addProductInputDto)) {
            throw new IllegalArgumentException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
        validateType(addProductInputDto.type());
        validatePrice(addProductInputDto.price());
        validateAddProductConsistency(addProductInputDto);
    }

    @Override
    public void validateAlterProductInputDto(AlterProductInputDto alterProductInputDto) {
        if (isAnyBlank(alterProductInputDto)) {
            throw new IllegalArgumentException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
        validateProductExistence(alterProductInputDto.name());
        validateType(alterProductInputDto.type());
        validatePrice(alterProductInputDto.price());
    }

    private boolean isAnyBlank(AddProductInputDto addProductInputDto) {
        return StringUtils.isAnyBlank(
                addProductInputDto.name(),
                addProductInputDto.type(),
                addProductInputDto.manufacturer(),
                addProductInputDto.price());
    }

    private boolean isAnyBlank(AlterProductInputDto alterProductInputDto) {
        return StringUtils.isAnyBlank(
                alterProductInputDto.name(),
                alterProductInputDto.type(),
                alterProductInputDto.manufacturer(),
                alterProductInputDto.price());
    }

    private void validateProductExistence(String name) {
        if (!productRepository.existsByName(name)) {
            throw new NoSuchElementException(NO_PRODUCT_FOUND_BY_NAME.getMessage() + name);
        }
    }

    private void validateType(String type) {
        if (!EnumUtils.isValidEnum(Genre.class, type)) {
            throw new IllegalArgumentException(INVALID_GENRE.getMessage() + type);
        }
    }

    private void validatePrice(String stringPrice) {
        try {
            BigDecimal price = new BigDecimal(stringPrice);
            if (price.signum() <= 0) {
                throw new IllegalArgumentException(PRICE_IS_NON_POSITIVE.getMessage() + price);
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException(PRICE_NOT_A_NUMBER.getMessage() + stringPrice);
        }
    }

    private void validateAddProductConsistency(AddProductInputDto addProductInputDto) {
        productRepository.findFirstByName(addProductInputDto.name())
                .ifPresent(product -> {
                    if (!areProductAndProductInputEqual(addProductInputDto, product)) {
                        throwConsistencyException(addProductInputDto.name(), product);
                    }
                });
    }

    private boolean areProductAndProductInputEqual(AddProductInputDto inputDto, Product product) {
        return inputDto.name().equals(product.getName()) &&
                inputDto.type().equals(product.getType().name()) &&
                inputDto.manufacturer().equals(product.getManufacturer()) &&
                inputDto.price().equals(product.getPrice().toPlainString());
    }

    private void throwConsistencyException(String productInputName, Product product) {
        throw new IllegalArgumentException(PRODUCT_IS_NOT_CONSISTENT.getMessage() +
                "(entered product's name: " + productInputName + ", " +
                "existing conflicting product: " +
                product.getName() + ", " +
                product.getType() + ", " +
                product.getManufacturer() + ", " +
                product.getPrice() + ")");
    }
}