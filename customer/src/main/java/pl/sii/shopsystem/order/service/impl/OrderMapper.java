package pl.sii.shopsystem.order.service.impl;

import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.order.dto.OrderInputDto;
import pl.sii.shopsystem.order.dto.OrderOutputDto;
import pl.sii.shopsystem.order.model.Order;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductInputDto;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductOutputDto;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;
import pl.sii.shopsystem.order.orderProduct.model.OrderProductKey;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OrderMapper {

    private final TimeSupplier timeSupplier;

    public OrderMapper(TimeSupplier timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    OrderOutputDto mapToOrderOutputDto(Customer customer, List<OrderProductOutputDto> purchasedProducts, BigDecimal totalCost) {
        return OrderOutputDto.builder()
                .customerFirstname(customer.getFirstname())
                .customerLastname(customer.getLastname())
                .customerEmail(customer.getEmail())
                .orderedProducts(purchasedProducts)
                .totalCost(totalCost)
                .build();
    }

    List<OrderProduct> mapToOrderProducts(List<ProductQuantity> productQuantities, Order order) {
        return productQuantities
                .stream()
                .map(productQuantity -> OrderProduct.builder()
                        .orderProductKey(
                                OrderProductKey.builder()
                                        .productId(productQuantity.product().getId())
                                        .orderId(order.getId())
                                        .build())
                        .product(productQuantity.product())
                        .order(order)
                        .quantity(productQuantity.quantity())
                        .build())
                .toList();
    }

    List<OrderProductOutputDto> mapToOrderProductOutputDtoList(List<ProductQuantity> productQuantities) {
        return productQuantities
                .stream()
                .map(productQuantity -> OrderProductOutputDto.builder()
                        .title(productQuantity.product().getTitle())
                        .type(productQuantity.product().getType())
                        .manufacturer(productQuantity.product().getManufacturer())
                        .price(productQuantity.product().getPrice())
                        .quantity(productQuantity.quantity())
                        .build())
                .toList();
    }

    List<ProductQuantity> mapToProductQuantities(
            OrderInputDto orderInputDto,
            Function<OrderProductInputDto, Optional<ProductQuantity>> mapToOptionalOfProductQuantity) {
        return orderInputDto.orderProducts()
                .stream()
                .map(mapToOptionalOfProductQuantity)
                .flatMap(Optional::stream)
                .toList();
    }

    Order mapToOrder(Customer customer, BigDecimal totalCost) {
        return Order.builder()
                .customer(customer)
                .cost(totalCost)
                .dateTime(timeSupplier.get())
                .build();
    }
}