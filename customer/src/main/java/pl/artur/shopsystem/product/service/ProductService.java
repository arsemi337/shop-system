package pl.artur.shopsystem.product.service;

import kafka.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.dto.ProductTypeNumberOutputDto;
import product.model.Genre;

public interface ProductService {

    Page<ProductOutputDto> fetchAllProducts(Pageable pageable);
    ProductOutputDto fetchProductById(String id);
    ProductTypeNumberOutputDto getNumberOfProductsByGenre(Genre genre);
    void saveProduct(ProductDto productDto);
    void updateProduct(ProductDto productDto);
    void removeProduct(ProductDto productDto);
}
