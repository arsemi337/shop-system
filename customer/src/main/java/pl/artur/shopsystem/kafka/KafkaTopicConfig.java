package pl.artur.shopsystem.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Profile("devProfile")
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic productTopic() {
        return TopicBuilder.name("product")
                .build();
    }
}
