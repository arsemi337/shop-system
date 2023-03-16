package pl.sii.shopsystem.kafka;

import kafka.ProductHeader;
import kafka.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.product.service.ProductService;

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
        productService.saveProduct(productDto);
        logger.info("Listener received a product: " + productDto.title() + " with a header: " + ProductHeader.PRODUCT_CREATED);
    }

    private void updateProduct(ProductDto productDto) {
        productService.updateProduct(productDto);
        logger.info("Listener received a product: " + productDto.title() + " with a header: " + ProductHeader.PRODUCT_MODIFIED);
    }

    private void removeProduct(ProductDto productDto) {
        productService.removeProduct(productDto);
        logger.info("Listener received a product: " + productDto.title() + " with a header: " + ProductHeader.PRODUCT_REMOVED);
    }
}
