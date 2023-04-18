package pl.artur.shopsystem.product.service;

import kafka.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.artur.shopsystem.product.dto.ProductOutputDto;

public interface ProductService {

    Page<ProductOutputDto> fetchAllProducts(Pageable pageable);
    ProductOutputDto fetchProductById(String id);
    void saveProduct(ProductDto productDto);
    void updateProduct(ProductDto productDto);
    void removeProduct(ProductDto productDto);
}
