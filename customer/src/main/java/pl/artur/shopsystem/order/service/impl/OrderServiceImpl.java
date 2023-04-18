package pl.artur.shopsystem.order.service.impl;

import exception.order.OrderErrorResponse;
import exception.order.OrderException;
import exception.order.ProductErrorDto;
import order.OrderProductOutputDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import pl.artur.shopsystem.customer.model.Customer;
import pl.artur.shopsystem.customer.repository.CustomerRepository;
import pl.artur.shopsystem.order.dto.OrderInputDto;
import pl.artur.shopsystem.order.dto.OrderOutputDto;
import pl.artur.shopsystem.order.dto.ProductOrderOutput;
import pl.artur.shopsystem.order.model.Order;
import pl.artur.shopsystem.order.orderProduct.dto.OrderProductInputDto;
import pl.artur.shopsystem.order.repository.OrderRepository;
import pl.artur.shopsystem.order.service.OrderService;
import pl.artur.shopsystem.order.service.OrderValidator;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import reactor.core.publisher.Mono;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.*;

import static exception.CustomerExceptionMessages.NO_CUSTOMER_BY_EMAIL_FOUND;
import static exception.order.OrderExceptionMessages.NOT_ALL_PRODUCTS_AVAILABLE;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderValidator validator;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WebClient webClient;

    public OrderServiceImpl(OrderValidator validator,
                            CustomerRepository customerRepository,
                            ProductRepository productRepository,
                            OrderRepository orderRepository,
                            TimeSupplier timeSupplier,
                            WebClient webClient) {
        this.validator = validator;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.webClient = webClient;
        this.orderMapper = new OrderMapper(timeSupplier, productRepository, new OrderParser());
    }

    @Override
    @Transactional
    public OrderOutputDto makeOrder(OrderInputDto orderInputDto) {
        validator.validateOrderInputDto(orderInputDto);

        // Get order customer
        UUID customerId = UUID.fromString(orderInputDto.customerId());
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException(NO_CUSTOMER_BY_EMAIL_FOUND.getMessage()));

        List<ProductErrorDto> productErrorDtoList = new ArrayList<>();

        Map<ProductInfo, List<Product>> productInfoToProductListMap = orderInputDto.orderProducts().stream()
                .map(dto -> orderMapper.mapToProductInfoToProductListMap(dto, productErrorDtoList))
                .collect(toSet())
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (!productErrorDtoList.isEmpty()) {
            throw new OrderException(NOT_ALL_PRODUCTS_AVAILABLE.getMessage(), productErrorDtoList);
        }

        List<OrderProductInputDto> orderProducts = productInfoToProductListMap.entrySet().stream()
                .map(entry -> OrderProductInputDto.builder()
                        .productName(entry.getKey().name())
                        .quantity(String.valueOf(entry.getValue().size()))
                        .build())
                .toList();

        webClient.post()
                .uri("http://localhost:8085/api/v1/products/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(orderProducts))
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() != 200, clientResponse ->
                        clientResponse.bodyToMono(OrderErrorResponse.class)
                                .flatMap(error ->
                                        Mono.error(new OrderException(error.message(), error.productsFailedToBePurchase())))
                )
                .bodyToMono(OrderProductOutputDto[].class)
                .log()
                .block();

        // Calculate total order cost
        BigDecimal totalCost = calculateTotalCost(productInfoToProductListMap);

        // Create order object and save it in a database
        Order order = orderMapper.mapToOrder(customer, totalCost);
        orderRepository.save(order);

        productInfoToProductListMap.values().stream()
                .flatMap(List::stream)
                .forEach(product -> {
                    product.setOrder(order);
                    product.setDeleted(true);
                    productRepository.save(product);
                });

        return orderMapper.mapToOrderOutputDto(
                customer,
                orderMapper.mapToOrderProductOutputDtoList(productInfoToProductListMap),
                totalCost);
    }

    @Override
    public Page<ProductOrderOutput> fetchOrdersSummary(Pageable pageable) {
        return productRepository.findProductOrderSummary(pageable)
                .map(orderMapper::mapToProductOrderOutput);
    }

    private BigDecimal calculateTotalCost(Map<ProductInfo, List<Product>> productInfoToProductListMap) {
        final BigDecimal[] totalCost = {new BigDecimal(0)};

        productInfoToProductListMap.forEach((key, value) -> {
            BigDecimal quantity = new BigDecimal(value.size());
            BigDecimal price = key.price();

            BigDecimal productTotalCos = price.multiply(quantity);
            totalCost[0] = totalCost[0].add(productTotalCos);
        });

        return totalCost[0];
    }
}