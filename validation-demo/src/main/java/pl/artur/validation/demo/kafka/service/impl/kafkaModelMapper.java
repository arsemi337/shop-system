package pl.artur.validation.demo.kafka.service.impl;

import pl.artur.validation.demo.kafka.TestModel;

public class kafkaModelMapper {

    public TestModel mapToModel(String name, int number) {
        return TestModel.builder()
                .name(name)
                .number(number)
                .build();
    }
}
