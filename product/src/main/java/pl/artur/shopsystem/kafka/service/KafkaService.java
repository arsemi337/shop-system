package pl.artur.shopsystem.kafka.service;

import pl.artur.shopsystem.product.model.Product;

import java.util.List;

public interface KafkaService {
    void sendProductListToTopic(List<Product> products, String messageHeader);
//    void sendProductToTopic(Product product, String messageHeader);
}
