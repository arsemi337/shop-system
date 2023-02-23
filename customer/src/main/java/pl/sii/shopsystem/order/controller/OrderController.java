package pl.sii.shopsystem.order.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.shopsystem.order.dto.OrderInputDto;
import pl.sii.shopsystem.order.dto.OrderOutputDto;
import pl.sii.shopsystem.order.service.OrderService;

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