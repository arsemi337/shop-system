package pl.sii.shopsystem.kafka;

import kafka.ProductHeader;
import kafka.dto.ProductDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.product.service.ProductService;

@Component
public class ProductKafkaListener {

    private final ProductService productService;

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
                default -> System.out.println(eventType + " header is not handled by the system");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Message header exception: " + e.getMessage());
        }
    }

    private void addProduct(ProductDto productDto) {
        productService.saveProduct(productDto);
        System.out.println("Listener received a product: " + productDto.title() + " with a header: " + ProductHeader.PRODUCT_CREATED);
    }

    private void updateProduct(ProductDto productDto) {
        productService.updateProduct(productDto);
        System.out.println("Listener received a product: " + productDto.title() + " with a header: " + ProductHeader.PRODUCT_MODIFIED);
    }

    private void removeProduct(ProductDto productDto) {
        productService.removeProduct(productDto);
        System.out.println("Listener received a product: " + productDto.title() + " with a header: " + ProductHeader.PRODUCT_REMOVED);
    }
}
