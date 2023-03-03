package pl.sii.shopsystem.product.service;

import pl.sii.shopsystem.product.dto.ProductInputDto;

public interface ProductValidator {

    void validateProductInputDto(ProductInputDto productInputDto);
}
