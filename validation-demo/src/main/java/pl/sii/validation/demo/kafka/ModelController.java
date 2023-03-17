package pl.sii.validation.demo.kafka;

import pl.sii.validation.demo.kafka.service.KafkaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/model")
public class ModelController {

    private final KafkaService kafkaService;

    public ModelController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @PostMapping
    String sendModelToTopic() {
        kafkaService.sendModelToTopic("Sort of name", 10, "PRODUCT_CREATED");
        return "\"Sort of name\" model with number 10 has been sent to the topic with a PRODUCT_CREATED header";
    }
}
