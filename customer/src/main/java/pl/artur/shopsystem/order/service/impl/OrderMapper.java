package pl.artur.shopsystem.order.service.impl;

import exception.order.ProductErrorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.artur.shopsystem.customer.model.Customer;
import pl.artur.shopsystem.order.dto.OrderInputDto;
import pl.artur.shopsystem.order.dto.OrderOutputDto;
import pl.artur.shopsystem.order.dto.ProductOrderOutput;
import pl.artur.shopsystem.order.model.Order;
import pl.artur.shopsystem.order.model.ProductOrderSummary;
import pl.artur.shopsystem.order.orderProduct.dto.OrderProductInputDto;
import pl.artur.shopsystem.order.orderProduct.dto.OrderProductOutputDto;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class OrderMapper {

    private final TimeSupplier timeSupplier;
    private final ProductRepository productRepository;
    private final OrderParser parser;

    public OrderMapper(TimeSupplier timeSupplier, ProductRepository productRepository, OrderParser parser) {
        this.timeSupplier = timeSupplier;
        this.productRepository = productRepository;
        this.parser = parser;
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

    List<OrderProductOutputDto> mapToOrderProductOutputDtoList(
            Map<ProductInfo, List<Product>> productInfoToProductListMap) {
        return productInfoToProductListMap.entrySet().stream()
                .map(entry -> OrderProductOutputDto.builder()
                        .name(entry.getKey().name())
                        .type(entry.getKey().type())
                        .manufacturer(entry.getKey().manufacturer())
                        .price(entry.getKey().price())
                        .quantity(entry.getValue().size())
                        .build())
                .toList();
    }

    List<ProductInfo> mapToProductQuantities(
            OrderInputDto orderInputDto,
            Function<OrderProductInputDto, Optional<ProductInfo>> mapToOptionalOfProductQuantity) {
        return orderInputDto.orderProducts()
                .stream()
                .map(mapToOptionalOfProductQuantity)
                .flatMap(Optional::stream)
                .toList();
    }

    Order mapToOrder(Customer customer, BigDecimal totalCost) {
        return Order.builder()
                .customer(customer)
                .totalCost(totalCost)
                .creationTime(timeSupplier.get())
                .build();
    }

    Map.Entry<ProductInfo, List<Product>> mapToProductInfoToProductListMap(
            OrderProductInputDto orderProductInputDto,
            List<ProductErrorDto> errorDtoList) {
        int quantity = parser.parseQuantity(orderProductInputDto.quantity());
        String productName = orderProductInputDto.productName();

        Page<Product> productPage = productRepository.findAllByNameAndIsDeleted(
                productName,
                false,
                PageRequest.of(0,  quantity));

        List<Product> products = productPage.get().toList();

        if (products.size() < quantity) {
            errorDtoList.add(
                    ProductErrorDto.builder()
                            .productName(productName)
                            .remaining(products.size())
                            .build());
        }

        ProductInfo productInfo;
        if (products.size() > 0) {
             productInfo = ProductInfo.builder()
                    .name(products.get(0).getName())
                    .type(products.get(0).getType())
                    .manufacturer(products.get(0).getManufacturer())
                    .price(products.get(0).getPrice())
                    .build();
        } else {
            productInfo = ProductInfo.builder()
                    .name(productName)
                    .build();
        }

        return Map.entry(productInfo, products);
    }

    public ProductOrderOutput mapToProductOrderOutput(ProductOrderSummary orderSummary) {
        return ProductOrderOutput.builder()
                .name(orderSummary.getName())
                .type(orderSummary.getType())
                .manufacturer(orderSummary.getManufacturer())
                .number(orderSummary.getNumber())
                .build();
    }
}