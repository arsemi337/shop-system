package pl.artur.shopsystem.order.service;

import pl.artur.shopsystem.order.dto.OrderInputDto;

public interface OrderValidator {

    void validateOrderInputDto(OrderInputDto orderInputDto);
    void validateQuantity(int quantity);
}
