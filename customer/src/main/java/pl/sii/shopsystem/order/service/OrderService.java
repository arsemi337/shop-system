package pl.sii.shopsystem.order.service;

import pl.sii.shopsystem.order.dto.OrderInputDto;
import pl.sii.shopsystem.order.dto.OrderOutputDto;

public interface OrderService {

    OrderOutputDto makeOrder(OrderInputDto orderInputDto);
}
