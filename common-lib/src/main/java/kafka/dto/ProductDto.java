package kafka.dto;

import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import product.model.Genre;

import java.math.BigDecimal;

@Builder
public record ProductDto(
        String name,
        Genre type,
        String manufacturer,
        BigDecimal price
) {

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("type", type)
                .append("manufacturer", manufacturer)
                .append("price", price)
                .toString();
    }
}