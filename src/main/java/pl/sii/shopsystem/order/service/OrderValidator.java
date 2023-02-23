package pl.sii.shopsystem.order.service;

import pl.sii.shopsystem.order.dto.OrderInputDto;

public interface OrderValidator {

    void validateOrderInputDto(OrderInputDto orderInputDto);
    void validateQuantity(int quantity);
}
