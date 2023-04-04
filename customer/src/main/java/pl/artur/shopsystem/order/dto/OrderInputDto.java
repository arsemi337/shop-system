package pl.artur.shopsystem.order.dto;

import lombok.Builder;
import pl.artur.shopsystem.order.orderProduct.dto.OrderProductInputDto;

import java.util.List;

@Builder
public record OrderInputDto(
        String customerId,
        List<OrderProductInputDto> orderProducts) {
}