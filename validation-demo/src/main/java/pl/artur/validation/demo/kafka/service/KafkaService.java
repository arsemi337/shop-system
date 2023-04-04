package pl.artur.validation.demo.kafka.service;

public interface KafkaService {
    void sendModelToTopic(String name, int number, String messageHeader);
}
