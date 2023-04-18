package pl.artur.shopsystem.kafka.service;

import pl.artur.shopsystem.product.model.Product;

import java.util.Map;

public interface KafkaService {
    void sendProductListToTopic(Map<Product, Integer> productToProductCountMap, String messageHeader);
}
