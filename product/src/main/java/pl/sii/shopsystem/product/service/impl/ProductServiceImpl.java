package pl.sii.shopsystem.product.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.sii.shopsystem.product.dto.ProductInputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.model.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.product.service.ProductService;
import pl.sii.shopsystem.product.service.ProductValidator;
import supplier.TimeSupplier;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static exception.ProductExceptionMessages.NO_PRODUCT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductValidator validator;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    ProductServiceImpl(ProductValidator validator,
                       TimeSupplier timeSupplier, ProductRepository productRepository) {
        this.validator = validator;
        this.productRepository = productRepository;
        this.productMapper = new ProductMapper(timeSupplier);
    }

    @Override
    public List<ProductOutputDto> addProducts(List<ProductInputDto> productInputDtoList) {
        productInputDtoList.forEach(validator::validateProductInputDto);

        return productInputDtoList.stream()
                .map(productMapper::mapToProduct)
                .map(productRepository::save)
                .map(productMapper::mapToProductOutputDto)
                .toList();
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
    public ProductOutputDto updateProduct(ProductInputDto productInputDto) {
        return null;
    }

    @Override
    public void removeProduct(String productId) {
        UUID productUuid = UUID.fromString(productId);
        if (productRepository.existsById(productUuid)) {
            productRepository.findById(productUuid);
        }
    }
}
