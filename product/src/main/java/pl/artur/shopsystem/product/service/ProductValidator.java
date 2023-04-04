package pl.artur.shopsystem.product.service;

import pl.artur.shopsystem.product.dto.AddProductInputDto;
import pl.artur.shopsystem.product.dto.AlterProductInputDto;

import java.util.List;

public interface ProductValidator {

    void validateNumberOfProductsToBeAdded(List<AddProductInputDto> addProductInputDtoList);
    void validateAddProductInputDto(AddProductInputDto addProductInputDto);
    void validateAlterProductInputDto(AlterProductInputDto alterProductInputDto);
}
