package pl.sii.shopsystem.product.service.impl;

import kafka.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.sii.shopsystem.product.dto.ProductTypeNumberOutputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.product.service.ProductService;
import product.model.Genre;

import java.util.NoSuchElementException;
import java.util.UUID;

import static exception.ProductExceptionMessages.NO_PRODUCT_FOUND;

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
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));
    }

    @Override
    public ProductTypeNumberOutputDto getNumberOfProductsByGenre(Genre type) {
        return ProductTypeNumberOutputDto.builder()
                .type(type)
                .productsNumber(productRepository.countByType(type))
                .build();
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        productRepository.save(
                productMapper.mapToProduct(productDto));
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        productRepository.save(
                productMapper.mapToProduct(productDto));
    }

    @Override
    public void removeProduct(ProductDto productDto) {
        productRepository.save(
                productMapper.mapToProduct(productDto));
    }
}
