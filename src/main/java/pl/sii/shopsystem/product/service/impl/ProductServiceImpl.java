package pl.sii.shopsystem.product.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.exception.ProductException;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.product.service.ProductService;

import java.util.UUID;

import static pl.sii.shopsystem.product.exception.ProductExceptionMessages.NO_PRODUCT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.productMapper = new ProductMapper();
    }

    @Override
    public Page<ProductOutputDto> fetchAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::mapToProductOutputDto);
    }

    @Override
    public ProductOutputDto fetchProductById(String id) {
        UUID productId = UUID.fromString(id);
        return productRepository.findById(productId)
                .map(productMapper::mapToProductOutputDto)
                .orElseThrow(() -> new ProductException(NO_PRODUCT_FOUND.getMessage()));
    }
}
