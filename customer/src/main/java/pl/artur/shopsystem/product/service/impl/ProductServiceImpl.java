package pl.artur.shopsystem.product.service.impl;

import kafka.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.artur.shopsystem.product.dto.ProductOutputDto;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import pl.artur.shopsystem.product.service.ProductService;
import supplier.TimeSupplier;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.IntStream;

import static exception.ProductExceptionMessages.NO_PRODUCT_FOUND;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);


    ProductServiceImpl(ProductRepository productRepository,
                       TimeSupplier timeSupplier) {
        this.productRepository = productRepository;
        this.productMapper = new ProductMapper(timeSupplier);
    }

    @Override
    public Page<ProductOutputDto> fetchAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::mapToProductOutputDto);
    }

    @Override
    public ProductOutputDto fetchProductById(String id) {
        UUID productId = UUID.fromString(id);
        return productRepository.findByIdAndIsDeleted(productId, false)
                .map(productMapper::mapToProductOutputDto)
                .orElseThrow(() -> new NoSuchElementException(NO_PRODUCT_FOUND.getMessage()));
    }

    @Override
    public void saveProduct(ProductDto productDto) {
        IntStream.range(0, productDto.count())
                .forEach(i -> {
                    Product product = productMapper.mapToProduct(productDto);
                    productRepository.save(product);
                });
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        List<Product> products = productRepository.findAllByNameAndIsDeleted(productDto.name(), false);
        if (products.size() != productDto.count()) {
            logger.warn("Products inconsistency between services. Product service has " +
                    productDto.count() + " " + productDto.name() +
                    " products to be updated. But Customer service has " +
                    products.size() + " products in a database");
        }
        products.forEach(product -> {
            product.setName(productDto.name());
            product.setType(productDto.type());
            product.setManufacturer(productDto.manufacturer());
            product.setPrice(productDto.price());
        });
        productRepository.saveAll(products);
    }

    @Override
    public void removeProduct(ProductDto productDto) {
        List<Product> products = productRepository.findAllByNameAndIsDeleted(
                        productDto.name(),
                        false,
                        PageRequest.of(0, productDto.count()))
                .toList();
        products.forEach(productRepository::delete);
    }
}
