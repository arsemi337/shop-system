package pl.artur.shopsystem.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import kafka.dto.ProductDto;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Profile("devProfile")
public class ProductDtoListDeserializer extends JsonDeserializer<List<ProductDto>> {

    private final Logger logger = LoggerFactory.getLogger(ProductKafkaListener.class);

    @Override
    public List<ProductDto> deserialize(String topic, Headers headers, byte[] data) {
        return deserialize(topic, data);
    }

    @Override
    public List<ProductDto> deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, new TypeReference<>() {
            });
        } catch (IOException e) {
            logger.error("An object received from topic [" + topic + "] couldn't be deserialized. Exception message: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
