package pl.artur.shopsystem.order.service;

import pl.artur.shopsystem.order.dto.OrderInputDto;
import pl.artur.shopsystem.order.dto.OrderOutputDto;

public interface OrderService {

    OrderOutputDto makeOrder(OrderInputDto orderInputDto);
}
