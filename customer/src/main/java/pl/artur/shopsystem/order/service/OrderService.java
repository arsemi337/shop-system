package pl.artur.shopsystem.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.artur.shopsystem.order.dto.OrderInputDto;
import pl.artur.shopsystem.order.dto.OrderOutputDto;
import pl.artur.shopsystem.order.dto.ProductOrderOutput;

public interface OrderService {

    OrderOutputDto makeOrder(OrderInputDto orderInputDto);
    Page<ProductOrderOutput> fetchOrdersSummary(Pageable pageable);

}
