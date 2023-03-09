package pl.sii.shopsystem.kafka;

import kafka.ProductHeader;
import kafka.dto.ProductDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "product",
            groupId = "groupId",
            containerFactory = "productKafkaListenerContainerFactory")
    void listener(@Payload ProductDto data,
                  @Header("EVENT_TYPE") String eventType) {
        switch (ProductHeader.valueOf(eventType)) {
            case PRODUCT_CREATED ->
                    System.out.println("Listener received a product: " + data.title() + " with a header: " + ProductHeader.PRODUCT_CREATED);
            case PRODUCT_MODIFIED ->
                    System.out.println("Listener received a product: " + data.title() + " with a header: " + ProductHeader.PRODUCT_MODIFIED);
            case PRODUCT_REMOVED ->
                    System.out.println("Listener received a product: " + data.title() + " with a header: " + ProductHeader.PRODUCT_REMOVED);
            default -> System.out.println("something went wrong");
        }
    }
}
