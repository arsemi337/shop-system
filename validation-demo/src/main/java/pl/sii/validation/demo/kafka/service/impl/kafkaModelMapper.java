package pl.sii.validation.demo.kafka.service.impl;

import pl.sii.validation.demo.kafka.TestModel;

public class kafkaModelMapper {

    public TestModel mapToModel(String name, int number) {
        return TestModel.builder()
                .name(name)
                .number(number)
                .build();
    }
}
