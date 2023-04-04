package pl.artur.shopsystem.product.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.artur.shopsystem.product.dto.*;
import pl.artur.shopsystem.product.repository.ProductRepository;
import pl.artur.shopsystem.product.service.ProductParser;
import pl.artur.shopsystem.product.service.ProductService;
import pl.artur.shopsystem.kafka.service.KafkaService;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.service.ProductValidator;
import product.model.Genre;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static exception.ProductExceptionMessages.NO_PRODUCT_FOUND;
import static exception.ProductExceptionMessages.NO_PRODUCT_FOUND_BY_NAME;
import static java.util.stream.Collectors.groupingBy;
import static kafka.ProductHeader.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductValidator validator;
    private final ProductParser parser;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final KafkaService kafkaService;

    ProductServiceImpl(ProductValidator validator,
                       ProductParser parser,
                       TimeSupplier timeSupplier,
                       ProductRepository productRepository,
                       KafkaService kafkaService) {
        this.validator = validator;
        this.parser = parser;
        this.productRepository = productRepository;
        this.productMapper = new ProductMapper(timeSupplier);
        this.kafkaService = kafkaService;
    }

    @Override
    @Transactional
    public List<MassProductOutputDto> addProducts(List<AddProductInputDto> addProductInputDtoList) {
        validator.validateNumberOfProductsToBeAdded(addProductInputDtoList);
        addProductInputDtoList.forEach(validator::validateAddProductInputDto);

        List<Product> products = addProductInputDtoList.stream()
                .flatMap(this::saveProductMultipleTimes)
                .toList();

        kafkaService.sendProductListToTopic(products, PRODUCT_CREATED.name());

        return products.stream()
                .collect(groupingBy(Product::getName))
                .values()
                .stream().map(productMapper::mapToMassProductOutputDto)
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
    public MassProductOutputDto updateProduct(AlterProductInputDto alterProductInputDto) {
        validator.validateAlterProductInputDto(alterProductInputDto);

        List<Product> products = productRepository.findAllByName(alterProductInputDto.name());

        products.forEach(product -> {
            product.setType(Genre.valueOf(alterProductInputDto.type()));
            product.setManufacturer(alterProductInputDto.manufacturer());
            product.setPrice(new BigDecimal(alterProductInputDto.price()));
        });

        productRepository.saveAll(products);
        List<Product> productsList = new ArrayList<>();
        productsList.add(products.get(0));
        kafkaService.sendProductListToTopic(productsList, PRODUCT_MODIFIED.name());

        return productMapper.mapToMassProductOutputDto(products);
    }

    @Override
    @Transactional
    public void removeProductsList(RemoveProductInputDto removeProductInputDto) {
        String name = removeProductInputDto.name();
        List<Product> products = productRepository.findAllByName(name);
        if (products.isEmpty()) {
            throw new NoSuchElementException(NO_PRODUCT_FOUND_BY_NAME.getMessage() + removeProductInputDto.name());
        }
        products.forEach(product -> product.setDeleted(true));
        List<Product> productsList = new ArrayList<>();
        productsList.add(products.get(0));
        kafkaService.sendProductListToTopic(productsList, PRODUCT_REMOVED.name());
    }

    @Override
    @Transactional
    public void removeProduct(String productId) {
        UUID productUuid = UUID.fromString(productId);
        Product product = productRepository.findById(productUuid)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));
        List<Product> productsList = new ArrayList<>();
        productsList.add(product);
        kafkaService.sendProductListToTopic(productsList, PRODUCT_REMOVED.name());
        productRepository.deleteById(product.getId());
    }

    private Stream<Product> saveProductMultipleTimes(AddProductInputDto addProductInputDto) {
        int number = parser.parseNumber(addProductInputDto.number());
        List<Product> products = new ArrayList<>();
        IntStream.range(0, number)
                .forEach(i -> {
                    Product product = productMapper.mapToProduct(addProductInputDto);
                    products.add(productRepository.save(product));
                });
        return products.stream();
    }
}
