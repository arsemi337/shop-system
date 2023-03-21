package pl.sii.shopsystem.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.core.log.LogAccessor;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.DeserializationException;

import java.util.Arrays;

@Profile("devProfile")
public class KafkaCustomerErrorHandler implements CommonErrorHandler {

    @Override
    public void handleRecord(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        LogAccessor logger = new LogAccessor(KafkaCustomerErrorHandler.class);
        DeserializationException exception = ReplyingKafkaTemplate.checkDeserialization(record, logger);
        if (exception != null) {
            logger.error("The following object couldn't be deserialized: " + Arrays.toString(exception.getData()));
        }
    }
}
