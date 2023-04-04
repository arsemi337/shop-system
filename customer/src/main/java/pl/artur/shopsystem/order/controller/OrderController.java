package pl.artur.shopsystem.order.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.artur.shopsystem.order.dto.OrderInputDto;
import pl.artur.shopsystem.order.dto.OrderOutputDto;
import pl.artur.shopsystem.order.service.OrderService;

@RestController
@Tag(name = "Order")
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    ResponseEntity<OrderOutputDto> makeOrder(@RequestBody OrderInputDto orderInputDto) {
        return ResponseEntity.ok(orderService.makeOrder(orderInputDto));
    }
}