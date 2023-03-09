package kafka.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProductDto(
        UUID id,
        LocalDateTime creationTime,
        String title,
        String type,
        String manufacturer,
        BigDecimal price,
        boolean isDeleted
) {
}