package pl.artur.shopsystem.product.service.impl;

import exception.order.OrderException;
import exception.order.ProductErrorDto;
import order.OrderProductInputDto;
import order.OrderProductOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.artur.shopsystem.kafka.service.KafkaService;
import pl.artur.shopsystem.product.dto.*;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import pl.artur.shopsystem.product.repository.ProductSpecification;
import pl.artur.shopsystem.product.service.ProductParser;
import pl.artur.shopsystem.product.service.ProductService;
import pl.artur.shopsystem.product.service.ProductValidator;
import product.model.Genre;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

import static exception.ProductExceptionMessages.*;
import static exception.order.OrderExceptionMessages.NOT_ALL_PRODUCTS_AVAILABLE;
import static java.util.stream.Collectors.toMap;
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
        this.productMapper = new ProductMapper(timeSupplier, productRepository, parser);
        this.kafkaService = kafkaService;
    }

    @Override
    @Transactional
    public List<MassProductOutputDto> addProducts(List<AddProductInputDto> addProductInputDtoList) {
        validator.validateNumberOfProductsToBeAdded(addProductInputDtoList);
        addProductInputDtoList.forEach(validator::validateAddProductInputDto);

        Map<Product, Integer> productToProductCountMap = addProductInputDtoList.stream()
                .map(this::saveProductMultipleTimes)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        kafkaService.sendProductListToTopic(productToProductCountMap, PRODUCT_CREATED.name());

        return productToProductCountMap.entrySet().stream()
                .map(productMapper::mapToMassProductOutputDto)
                .toList();
    }

    @Override
    public Page<ProductOutputDto> fetchAllProducts(
            String field,
            String operation,
            String value,
            Pageable pageable) {

        if (field == null && operation == null && value == null) {
            return productRepository.findAllByIsDeleted(pageable, false)
                    .map(productMapper::mapToProductOutputDto);
        } else if (field != null && operation != null && value != null) {
            LocalDateTime dateTime = validateFilteringParameters(field, operation, value);

            SearchCriteria criteria = SearchCriteria.builder()
                    .key(field)
                    .operation(operation)
                    .value(value)
                    .build();

            if (dateTime != null) {
                criteria.setValue(dateTime);
            }

            ProductSpecification spec = ProductSpecification.builder()
                    .criteria(criteria)
                    .build();

            return productRepository.findAll(spec, pageable)
                    .map(productMapper::mapToProductOutputDto);
        } else {
            throw new IllegalArgumentException(FILTERING_PARAMETERS_NOT_COMPLETE.getMessage());
        }
    }

    @Override
    public ProductOutputDto fetchProductById(String productId) {
        UUID productUuid = UUID.fromString(productId);
        return productRepository.findByIdAndIsDeleted(productUuid, false)
                .map(productMapper::mapToProductOutputDto)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));
    }

    @Override
    public ProductTypeNumberOutputDto getNumberOfProductsByGenre(Genre type) {
        return ProductTypeNumberOutputDto.builder()
                .type(type)
                .productsNumber(productRepository.countByTypeAndIsDeleted(type, false))
                .build();
    }

    @Override
    @Transactional
    public MassProductOutputDto updateAllSameProducts(AlterProductInputDto alterProductInputDto) {
        validator.validateAlterProductInputDto(alterProductInputDto);

        List<Product> products = productRepository.findAllByNameAndIsDeleted(alterProductInputDto.name(), false);

        products.forEach(product -> {
            product.setType(Genre.valueOf(alterProductInputDto.type()));
            product.setManufacturer(alterProductInputDto.manufacturer());
            product.setPrice(new BigDecimal(alterProductInputDto.price()));
        });

        productRepository.saveAll(products);
        Map<Product, Integer> productIntegerMap = Map.of(products.get(0), products.size());
        kafkaService.sendProductListToTopic(productIntegerMap, PRODUCT_MODIFIED.name());

        return productMapper.mapToMassProductOutputDto(Map.entry(products.get(0), products.size()));
    }

    @Override
    @Transactional
    public void removeProductsByName(String productName, String productsNumber) {
        List<Product> products;
        if (productsNumber == null) {
            products = productRepository.findAllByNameAndIsDeleted(productName, false);
        } else {
            int number = parser.parseProductsNumber(productsNumber);
            long databaseCount = productRepository.countByNameAndIsDeleted(productName, false);
            if (databaseCount < number) {
                throw new IllegalArgumentException(NOT_ENOUGH_PRODUCTS_TO_REMOVE.getMessage() +
                        " (tried: " + number + "; actually was: " + databaseCount + ")");
            }
            products = productRepository
                    .findAllByNameAndIsDeleted(productName, false, PageRequest.of(0, number))
                    .toList();
        }
        if (products.isEmpty()) {
            throw new NoSuchElementException(NO_PRODUCT_FOUND_BY_NAME.getMessage() + productName);
        }
        products.forEach(product -> product.setDeleted(true));
        Map<Product, Integer> productIntegerMap = Map.of(products.get(0), products.size());
        kafkaService.sendProductListToTopic(productIntegerMap, PRODUCT_REMOVED.name());
    }

    @Override
    public List<OrderProductOutputDto> purchaseProducts(List<OrderProductInputDto> orderProductInputDtoList) {
        orderProductInputDtoList
                .forEach(validator::validatePurchaseProductInputDto);

        List<ProductErrorDto> productErrorDtoList = new ArrayList<>();

        Map<String, List<Product>> productNameToProductListMap = orderProductInputDtoList.stream()
                .map(dto -> productMapper.mapToStringToProductListMap(dto, productErrorDtoList))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!productErrorDtoList.isEmpty()) {
            throw new OrderException(NOT_ALL_PRODUCTS_AVAILABLE.getMessage(), productErrorDtoList);
        }

        productNameToProductListMap.values().stream()
                .flatMap(List::stream)
                .forEach(product -> productRepository.deleteById(product.getId()));

        return productNameToProductListMap.entrySet().stream()
                .map(entry -> OrderProductOutputDto.builder()
                        .name(entry.getKey())
                        .quantity(entry.getValue().size())
                        .build())
                .toList();
    }

    private Map.Entry<Product, Integer> saveProductMultipleTimes(AddProductInputDto addProductInputDto) {
        int number = parser.parseAddProductsNumber(addProductInputDto.number());
        IntStream.range(0, number)
                .forEach(i -> {
                    Product product = productMapper.mapToProduct(addProductInputDto);
                    productRepository.save(product);
                });
        Product product = productMapper.mapToProduct(addProductInputDto);
        return Map.entry(product, number);
    }

    private LocalDateTime validateFilteringParameters(String field, String operation, String value) {
        LocalDateTime dateTime = null;
        switch (field) {
            case "name", "type", "manufacturer" -> {
                if (!operation.equals("=")) {
                    throw new IllegalArgumentException(ILLEGAL_STRING_FILTERING_OPERATION.getMessage() + "name, type, manufacturer");
                }
            }
            case "price" -> {
                if (!operation.equals("=") &&
                        !operation.equals(">") &&
                        !operation.equals("<")) {
                    throw new IllegalArgumentException(ILLEGAL_PRICE_FILTERING_OPERATION.getMessage() + "price");
                }
                parser.validateBigDecimal(value);
            }
            case "creationTime" -> {
                if (!operation.equals(">") &&
                        !operation.equals("<")) {
                    throw new IllegalArgumentException(ILLEGAL_TIME_FILTERING_OPERATION.getMessage() + "time");
                }
                LocalDate date = parser.parseLocalDate(value);
                dateTime = date.atStartOfDay();
            }
            default -> throw new IllegalArgumentException(ILLEGAL_FILTERING_PARAMETER.getMessage() + field);
        }
        return dateTime;
    }
}
