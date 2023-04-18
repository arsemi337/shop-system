package pl.artur.shopsystem.kafka;

import kafka.ProductHeader;
import kafka.dto.ProductDto;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.artur.shopsystem.product.service.ProductService;
import product.model.Genre;

import java.math.BigDecimal;
import java.util.List;

@Profile("devProfile")
@Component
public class ProductKafkaListener {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductKafkaListener.class);

    public ProductKafkaListener(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "products",
            groupId = "groupId",
            containerFactory = "productKafkaListenerContainerFactory")
    void productListener(@Payload List<ProductDto> data,
                         @Header("EVENT_TYPE") String eventType) {
        try {
            switch (ProductHeader.valueOf(eventType)) {
                case PRODUCT_CREATED -> addProducts(data);
                case PRODUCT_MODIFIED -> updateProduct(data);
                case PRODUCT_REMOVED -> removeProduct(data);
                default -> logger.info(eventType + " header is not handled by the system");
            }
        } catch (IllegalArgumentException e) {
            logger.info("Message header exception: " + e.getMessage());
        }
    }

    private void addProducts(List<ProductDto> productDtoList) {
        productDtoList.forEach(productDto -> {
            if (!isProductDtoValid(productDto)) {
                logger.warn("Invalid product couldn't be added to the database: " + productDto);
            } else {
                productService.saveProduct(productDto);
                logger.info("Listener received a product: " + productDto + " with a header: " + ProductHeader.PRODUCT_CREATED);
            }
        });
    }

    private void updateProduct(List<ProductDto> productDtoList) {
        productDtoList.forEach(productDto -> {
            if (!isProductDtoValid(productDto)) {
                logger.warn("Invalid product couldn't be updated in the database: " + productDto);
            } else {
                productService.updateProduct(productDto);
                logger.info("Listener received a product: " + productDto + " with a header: " + ProductHeader.PRODUCT_MODIFIED);
            }
        });
    }

    private void removeProduct(List<ProductDto> productDtoList) {
        productDtoList.forEach(productDto -> {
            if (!isProductDtoValid(productDto)) {
                logger.warn("Invalid product couldn't be removed from the database: " + productDto);
            } else {
                productService.removeProduct(productDto);
                logger.info("Listener received a product: " + productDto + " with a header: " + ProductHeader.PRODUCT_REMOVED);
            }
        });
    }

    private boolean isProductDtoValid(ProductDto productDto) {
        return !(StringUtils.isAnyBlank(
                productDto.name(),
                productDto.manufacturer()) &&
                productDto.price().compareTo(BigDecimal.ZERO) > 0 &&
                EnumUtils.isValidEnum(Genre.class, productDto.type().toString()));
    }
}
