package pl.sii.shopsystem.kafka;

import kafka.ProductHeader;
import kafka.dto.ProductDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.product.service.ProductService;

import java.math.BigDecimal;

@Profile("devProfile")
@Component
public class ProductKafkaListener {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductKafkaListener.class);

    public ProductKafkaListener(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "product",
            groupId = "groupId",
            containerFactory = "productKafkaListenerContainerFactory")
    void productListener(@Payload ProductDto data,
                         @Header("EVENT_TYPE") String eventType) {
        try {
            switch (ProductHeader.valueOf(eventType)) {
                case PRODUCT_CREATED -> addProduct(data);
                case PRODUCT_MODIFIED -> updateProduct(data);
                case PRODUCT_REMOVED -> removeProduct(data);
                default -> logger.info(eventType + " header is not handled by the system");
            }
        } catch (IllegalArgumentException e) {
            logger.info("Message header exception: " + e.getMessage());
        }
    }

    private void addProduct(ProductDto productDto) {
        if (isProductDtoValid(productDto)) {
            logger.warn("Invalid product couldn't be added to the database:\n" + productDto);
        }
        productService.saveProduct(productDto);
        logger.info("Listener received a product:\n" + productDto + "\nwith a header: " + ProductHeader.PRODUCT_CREATED);
    }

    private void updateProduct(ProductDto productDto) {
        if (isProductDtoValid(productDto)) {
            logger.warn("Invalid product couldn't be added to the database:\n" + productDto);
        }
        productService.updateProduct(productDto);
        logger.info("Listener received a product:\n" + productDto + "\nwith a header: " + ProductHeader.PRODUCT_MODIFIED);
    }

    private void removeProduct(ProductDto productDto) {
        if (isProductDtoValid(productDto)) {
            logger.warn("Invalid product couldn't be added to the database:\n" + productDto);
        }
        productService.removeProduct(productDto);
        logger.info("Listener received a product:\n" + productDto + "\nwith a header: " + ProductHeader.PRODUCT_REMOVED);
    }

    private boolean isProductDtoValid(ProductDto productDto) {
        return productDto.id() == null ||
                StringUtils.isAnyBlank(
                        productDto.title(),
                        productDto.type(),
                        productDto.publishingHouse()) ||
                productDto.creationTime() == null ||
                productDto.price().compareTo(BigDecimal.ZERO) < 1;
    }
}
