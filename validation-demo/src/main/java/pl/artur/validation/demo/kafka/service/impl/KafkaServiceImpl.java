package pl.artur.validation.demo.kafka.service.impl;

import pl.artur.validation.demo.kafka.KafkaTopicConfig;
import pl.artur.validation.demo.kafka.TestModel;
import pl.artur.validation.demo.kafka.service.KafkaService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

@Service
public class KafkaServiceImpl implements KafkaService {
    private final KafkaTemplate<String, TestModel> kafkaTemplate;
    private final KafkaTopicConfig topicConfig;
    private final kafkaModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    public KafkaServiceImpl(KafkaTemplate<String, TestModel> kafkaTemplate,
                            KafkaTopicConfig topicConfig) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicConfig = topicConfig;
        this.modelMapper = new kafkaModelMapper();
    }

    @Override
    public void sendModelToTopic(String name, int number, String messageHeader) {
        TestModel dto = modelMapper.mapToModel(name, number);
        String topicName = topicConfig.productTopic().name();

        // Create message with custom header
        var producerRecord = new ProducerRecord<String, TestModel>(topicName, dto);
        producerRecord.headers().add("EVENT_TYPE", messageHeader.getBytes(StandardCharsets.UTF_8));

        var future = kafkaTemplate.send(producerRecord);
        future.whenComplete(whenCompleteFunction());
    }

    private BiConsumer<SendResult<String, TestModel>, Throwable> whenCompleteFunction() {
        return (result, ex) -> {
            if (ex == null) {
                logger.info(result.getProducerRecord().value().name() +
                        " product has been sent to the " +
                        result.getProducerRecord().topic() +
                        " topic successfully");
            } else {
                logger.info(ex.getMessage());
            }
        };
    }
}
