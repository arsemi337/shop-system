package pl.sii.shopsystem.kafka.service.impl;

import kafka.dto.ProductDto;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import pl.sii.shopsystem.kafka.KafkaTopicConfig;
import pl.sii.shopsystem.kafka.service.KafkaService;
import pl.sii.shopsystem.product.model.Product;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

@Service
public class KafkaServiceImpl implements KafkaService {
    private final KafkaTemplate<String, ProductDto> kafkaTemplate;
    private final KafkaTopicConfig topicConfig;
    private final KafkaProductMapper productMapper;

    public KafkaServiceImpl(KafkaTemplate<String, ProductDto> kafkaTemplate,
                            KafkaTopicConfig topicConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicConfig = topicConfig;
        this.productMapper = new KafkaProductMapper();
    }

    @Override
    public void sendProductToTopic(Product product, String messageHeader) {
        ProductDto dto = productMapper.mapToProductDto(product);
        String topicName = topicConfig.productTopic().name();

        // Create message with custom header
        var producerRecord = new ProducerRecord<String, ProductDto>(topicName, dto);
        producerRecord.headers().add("EVENT_TYPE", messageHeader.getBytes(StandardCharsets.UTF_8));

        var future = kafkaTemplate.send(producerRecord);
        future.whenComplete(whenCompleteFunction(product));
    }

    private static BiConsumer<SendResult<String, ProductDto>, Throwable> whenCompleteFunction(Product product) {
        return (result, ex) -> {
            if (ex == null) {
                System.out.println(result.getProducerRecord().value().title() +
                        " product has been sent to the " +
                        result.getProducerRecord().topic() +
                        " topic successfully");
            } else {
                System.out.println(ex.getMessage());
            }
        };
    }
}
