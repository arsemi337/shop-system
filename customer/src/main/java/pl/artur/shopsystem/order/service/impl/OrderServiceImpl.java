package pl.artur.shopsystem.order.service.impl;

import exception.order.ProductErrorDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.artur.shopsystem.customer.model.Customer;
import pl.artur.shopsystem.customer.repository.CustomerRepository;
import pl.artur.shopsystem.order.dto.OrderInputDto;
import pl.artur.shopsystem.order.dto.OrderOutputDto;
import pl.artur.shopsystem.order.model.Order;
import pl.artur.shopsystem.order.repository.OrderRepository;
import pl.artur.shopsystem.order.service.OrderService;
import pl.artur.shopsystem.order.service.OrderValidator;
import pl.artur.shopsystem.product.model.Product;
import pl.artur.shopsystem.product.repository.ProductRepository;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.*;

import static exception.CustomerExceptionMessages.NO_CUSTOMER_BY_EMAIL_FOUND;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderValidator validator;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    public OrderServiceImpl(OrderValidator validator,
                            CustomerRepository customerRepository,
                            ProductRepository productRepository,
                            OrderRepository orderRepository,
                            TimeSupplier timeSupplier) {
        this.validator = validator;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.mapper = new OrderMapper(timeSupplier, productRepository, new OrderParser());
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
                .map(dto -> mapper.mapToProductInfoToProductListMap(dto, productErrorDtoList))
                .collect(toSet())
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Calculate total order cost
        BigDecimal totalCost = calculateTotalCost(productInfoToProductListMap);

        // Create order object and save it in a database
        Order order = mapper.mapToOrder(customer, totalCost);
        orderRepository.save(order);

        productInfoToProductListMap.values().stream()
                .flatMap(List::stream)
                .forEach(product -> {
                    product.setOrder(order);
                    product.setDeleted(true);
                    productRepository.save(product);
                });

        return mapper.mapToOrderOutputDto(
                customer,
                mapper.mapToOrderProductOutputDtoList(productInfoToProductListMap),
                totalCost);
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