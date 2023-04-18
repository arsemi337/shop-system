package pl.artur.shopsystem.kafka.service.impl;

import kafka.dto.ProductDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import pl.artur.shopsystem.kafka.KafkaTopicConfig;
import pl.artur.shopsystem.kafka.service.KafkaService;
import pl.artur.shopsystem.product.model.Product;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
public class KafkaServiceImpl implements KafkaService {
    private final KafkaTemplate<String, List<ProductDto>> kafkaTemplate;
    private final KafkaTopicConfig topicConfig;
    private final KafkaProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    public KafkaServiceImpl(KafkaTemplate<String, List<ProductDto>> kafkaTemplate,
                            KafkaTopicConfig topicConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicConfig = topicConfig;
        this.productMapper = new KafkaProductMapper();
    }

    @Override
    public void sendProductListToTopic(Map<Product, Integer> productToProductCountMap, String messageHeader) {
        String topicName = topicConfig.productsTopic().name();
        List<ProductDto> productDtoList = productToProductCountMap.entrySet().stream()
                .map(productMapper::mapToProductDto)
                .toList();

        // Create message with custom header
        var productsRecord = new ProducerRecord<String, List<ProductDto>>(topicName, productDtoList);
        productsRecord.headers().add("EVENT_TYPE", messageHeader.getBytes(StandardCharsets.UTF_8));

        var future = kafkaTemplate.send(productsRecord);
        future.whenComplete(whenCompleteFunction());
    }

    private BiConsumer<SendResult<String, List<ProductDto>>, Throwable> whenCompleteFunction() {
        return (result, ex) -> {
            if (ex == null) {
                result.getProducerRecord().value()
                        .forEach(productDto ->
                                logger.info(productDto.name() +
                                        " product has been sent to the " +
                                        result.getProducerRecord().topic() +
                                        " topic successfully"));
            } else {
                logger.info(ex.getMessage());
            }
        };
    }
}
