package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KafkaExceptionMessages {
    INVALID_MESSAGE_HEADER("Message");

    private final String message;
}
