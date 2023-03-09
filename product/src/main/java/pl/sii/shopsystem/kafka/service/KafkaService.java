package pl.sii.shopsystem.kafka.service;

import pl.sii.shopsystem.product.model.Product;

public interface KafkaService {
    void sendProductToTopic(Product product, String messageHeader);
}
