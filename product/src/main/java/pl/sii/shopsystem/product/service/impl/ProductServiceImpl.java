package pl.sii.shopsystem.product.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sii.shopsystem.kafka.service.KafkaService;
import pl.sii.shopsystem.product.dto.ProductInputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.model.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.product.service.ProductService;
import pl.sii.shopsystem.product.service.ProductValidator;
import product.model.Genre;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static exception.ProductExceptionMessages.NO_PRODUCT_FOUND;
import static kafka.ProductHeader.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductValidator validator;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaService kafkaService;

    ProductServiceImpl(ProductValidator validator,
                       TimeSupplier timeSupplier,
                       ProductRepository productRepository,
                       KafkaService kafkaService) {
        this.validator = validator;
        this.productRepository = productRepository;
        this.productMapper = new ProductMapper(timeSupplier);
        this.kafkaService = kafkaService;
    }

    @Override
    @Transactional
    public List<ProductOutputDto> addProducts(List<ProductInputDto> productInputDtoList) {
        productInputDtoList.forEach(validator::validateProductInputDto);
        productInputDtoList.forEach(validator::validateProductExistence);

        List<Product> products = productInputDtoList.stream()
                .map(productMapper::mapToProduct)
                .map(productRepository::save).
                toList();

        products.forEach(product -> kafkaService.sendProductToTopic(product, PRODUCT_CREATED.name()));

        return products.stream()
                .map(productMapper::mapToProductOutputDto)
                .toList();
    }

    @Override
    public Page<ProductOutputDto> fetchAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::mapToProductOutputDto);
    }

    @Override
    public ProductOutputDto fetchProductById(String productId) {
        UUID productUuid = UUID.fromString(productId);
        return productRepository.findById(productUuid)
                .map(productMapper::mapToProductOutputDto)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));
    }

    @Override
    @Transactional
    public ProductOutputDto updateProduct(String productId, ProductInputDto productInputDto) {
        validator.validateProductInputDto(productInputDto);

        UUID productUuid = UUID.fromString(productId);
        Product product = productRepository.findById(productUuid)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));

        validator.validateProductTitleChange(product.getTitle(), productInputDto.title());
        product.setTitle(productInputDto.title());
        product.setGenre(Genre.valueOf(productInputDto.genre()));
        product.setPublishingHouse(productInputDto.publishingHouse());
        product.setPrice(new BigDecimal(productInputDto.price()));

        productRepository.save(product);
        kafkaService.sendProductToTopic(product, PRODUCT_MODIFIED.name());

        return productMapper.mapToProductOutputDto(product);
    }

    @Override
    @Transactional
    public void removeProduct(String productId) {
        UUID productUuid = UUID.fromString(productId);
        Product product = productRepository.findById(productUuid)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));
        kafkaService.sendProductToTopic(product, PRODUCT_REMOVED.name());
        productRepository.deleteById(product.getId());
    }
}
