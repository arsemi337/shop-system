package pl.artur.shopsystem.order.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.artur.shopsystem.order.dto.OrderInputDto;
import pl.artur.shopsystem.order.dto.OrderOutputDto;
import pl.artur.shopsystem.order.dto.ProductOrderOutput;
import pl.artur.shopsystem.order.service.OrderService;

@RestController
@Tag(name = "Order")
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    ResponseEntity<OrderOutputDto> makeOrder(@RequestBody OrderInputDto orderInputDto) {
        return ResponseEntity.ok(orderService.makeOrder(orderInputDto));
    }

    @PostMapping("/summary")
    ResponseEntity<Page<ProductOrderOutput>> fetchOrdersSummary(@Parameter Pageable pageable) {
        return ResponseEntity.ok(orderService.fetchOrdersSummary(pageable));
    }
}