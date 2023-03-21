package pl.sii.shopsystem.product.service;

import kafka.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.sii.shopsystem.product.dto.ProductTypeNumberOutputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import product.model.Genre;

public interface ProductService {

    Page<ProductOutputDto> fetchAllProducts(Pageable pageable);
    ProductOutputDto fetchProductById(String id);
    ProductTypeNumberOutputDto getNumberOfProductsByGenre(Genre genre);
    void saveProduct(ProductDto productDto);
    void updateProduct(ProductDto productDto);
    void removeProduct(ProductDto productDto);
}
