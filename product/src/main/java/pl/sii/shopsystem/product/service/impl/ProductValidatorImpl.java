package pl.sii.shopsystem.product.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.product.dto.ProductInputDto;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.product.service.ProductValidator;

import java.math.BigDecimal;

import static exception.CustomerExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static exception.ProductExceptionMessages.*;

@Component
public class ProductValidatorImpl implements ProductValidator {

    private final ProductRepository productRepository;

    public ProductValidatorImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void validateProductInputDto(ProductInputDto productInputDto) {
        if (isAnyBlank(productInputDto)) {
            throw new IllegalArgumentException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
        validatePrice(productInputDto.price());
    }

    @Override
    public void validateProductExistence(ProductInputDto productInputDto) {
        if (productRepository.existsByTitle(productInputDto.title())) {
            throw new IllegalArgumentException(PRODUCT_ALREADY_EXISTS.getMessage() + productInputDto.title());
        }
    }

    @Override
    public void validateProductTitleChange(String oldTitle, String newTitle) {
        if (oldTitle.equals(newTitle)) {
            return;
        }
        if (productRepository.existsByTitle(newTitle)) {
            throw new IllegalArgumentException(PRODUCT_ALREADY_EXISTS.getMessage());
        }
    }

    private boolean isAnyBlank(ProductInputDto productInputDto) {
        return StringUtils.isAnyBlank(
                productInputDto.title(),
                productInputDto.type(),
                productInputDto.manufacturer(),
                productInputDto.price());
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
}