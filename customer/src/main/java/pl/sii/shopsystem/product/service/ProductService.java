package pl.sii.shopsystem.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.sii.shopsystem.product.dto.ProductOutputDto;

public interface ProductService {

    Page<ProductOutputDto> fetchAllProducts(Pageable pageable);
    ProductOutputDto fetchProductById(String id);
}
