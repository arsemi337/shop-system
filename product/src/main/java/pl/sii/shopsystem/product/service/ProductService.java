package pl.sii.shopsystem.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.sii.shopsystem.product.dto.ProductInputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;

import java.util.List;

public interface ProductService {

    List<ProductOutputDto> addProducts(List<ProductInputDto> productInputDtoList);
    Page<ProductOutputDto> fetchAllProducts(Pageable pageable);
    ProductOutputDto fetchProductById(String id);
    ProductOutputDto updateProduct(String productId, ProductInputDto productInputDto);
    void removeProduct(String productId);
}
