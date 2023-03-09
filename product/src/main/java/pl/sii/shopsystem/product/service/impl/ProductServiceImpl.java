package pl.sii.shopsystem.product.service.impl;

import dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sii.shopsystem.product.dto.ProductInputDto;
import pl.sii.shopsystem.product.dto.ProductOutputDto;
import pl.sii.shopsystem.product.model.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;
import pl.sii.shopsystem.product.service.ProductService;
import pl.sii.shopsystem.product.service.ProductValidator;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static exception.ProductExceptionMessages.NO_PRODUCT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {

    private final KafkaTemplate<String, ProductDto> kafkaTemplate;
    private final ProductValidator validator;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    ProductServiceImpl(KafkaTemplate<String, ProductDto> kafkaTemplate,
                       ProductValidator validator,
                       TimeSupplier timeSupplier,
                       ProductRepository productRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.validator = validator;
        this.productRepository = productRepository;
        this.productMapper = new ProductMapper(timeSupplier);
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

        products.forEach(product -> kafkaTemplate.send("product", productMapper.mapToProductDto(product)));

//        List<ProductDto> productDtoList = products.stream()
//                .map(productMapper::mapToProductDto)
//                .toList();

//        kafkaTemplate.send("product", productInputDtoList.get(0).title());

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
    public ProductOutputDto updateProduct(String productId, ProductInputDto productInputDto) {
        validator.validateProductInputDto(productInputDto);

        UUID productUuid = UUID.fromString(productId);
        Product product = productRepository.findById(productUuid)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));

        validator.validateProductTitleChange(product.getTitle(), productInputDto.title());
        product.setTitle(productInputDto.title());
        product.setType(productInputDto.type());
        product.setManufacturer(productInputDto.manufacturer());
        product.setPrice(new BigDecimal(productInputDto.price()));

        productRepository.save(product);

        return productMapper.mapToProductOutputDto(product);
    }

    @Override
    public void removeProduct(String productId) {
        UUID productUuid = UUID.fromString(productId);
        Product product = productRepository.findById(productUuid)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));
        productRepository.deleteById(product.getId());
    }
}
