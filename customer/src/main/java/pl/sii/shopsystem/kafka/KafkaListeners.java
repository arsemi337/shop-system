package pl.sii.shopsystem.kafka;

import dto.ProductDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "product",
            groupId = "groupId",
            containerFactory = "productKafkaListenerContainerFactory")
    void listener(ProductDto data) {
        System.out.println("Listener received product: " + data.title() + " :)");
    }
}
