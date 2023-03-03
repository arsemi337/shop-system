package pl.sii.shopsystem.order.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.customer.repository.CustomerRepository;
import pl.sii.shopsystem.order.dto.OrderInputDto;
import pl.sii.shopsystem.order.dto.OrderOutputDto;
import pl.sii.shopsystem.order.model.Order;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductInputDto;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;
import pl.sii.shopsystem.order.orderProduct.repository.OrderProductRepository;
import pl.sii.shopsystem.order.repository.OrderRepository;
import pl.sii.shopsystem.order.service.OrderService;
import pl.sii.shopsystem.order.service.OrderValidator;
import pl.sii.shopsystem.product.model.Product;
import pl.sii.shopsystem.product.repository.ProductRepository;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static exception.CustomerExceptionMessages.NO_CUSTOMER_BY_EMAIL_FOUND;
import static exception.OrderExceptionMessages.PRODUCT_NOT_FOUND;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderValidator validator;
    private final OrderParser parser;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper mapper;

    public OrderServiceImpl(OrderValidator validator,
                            CustomerRepository customerRepository,
                            ProductRepository productRepository,
                            OrderRepository orderRepository,
                            OrderProductRepository orderProductRepository,
                            TimeSupplier timeSupplier) {
        this.validator = validator;
        this.parser = new OrderParser();
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
        this.mapper = new OrderMapper(timeSupplier);
    }

    @Override
    @Transactional
    public OrderOutputDto makeOrder(OrderInputDto orderInputDto) {
        validator.validateOrderInputDto(orderInputDto);

        // Get order customer
        UUID customerId = UUID.fromString(orderInputDto.customerId());
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException(NO_CUSTOMER_BY_EMAIL_FOUND.getMessage()));

        // Create a list of objects, each of which contains a desired product and its quantity
        List<ProductQuantity> productQuantities = mapper.mapToProductQuantities(
                orderInputDto,
                this::createProductQuantity);

        // Calculate total order cost
        BigDecimal totalCost = calculateTotalCost(productQuantities);

        // Create order object and save it in a database
        Order order = mapper.mapToOrder(customer, totalCost);
        orderRepository.save(order);

        // Create OrderProduct objects and save them in a database
        List<OrderProduct> orderProducts = mapper.mapToOrderProducts(productQuantities, order);
        orderProducts.forEach(orderProductRepository::save);

        return mapper.mapToOrderOutputDto(
                customer,
                mapper.mapToOrderProductOutputDtoList(productQuantities),
                totalCost);
    }

    private BigDecimal calculateTotalCost(List<ProductQuantity> productQuantities) {
        BigDecimal totalCost = new BigDecimal(0);
        BigDecimal quantity;
        BigDecimal price;

        for (ProductQuantity productQuantity : productQuantities) {
            quantity = new BigDecimal(productQuantity.quantity());
            price = productQuantity.product().getPrice();

            BigDecimal productCost = price.multiply(quantity);
            totalCost = totalCost.add(productCost);
        }
        return totalCost;
    }

    private Optional<ProductQuantity> createProductQuantity(OrderProductInputDto inputDto) {
        UUID uuid = UUID.fromString(inputDto.productId());
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new NoSuchElementException(PRODUCT_NOT_FOUND.getMessage()));

        int quantity = parser.parseQuantity(inputDto.quantity());
        validator.validateQuantity(quantity);

        ProductQuantity productQuantity = ProductQuantity.builder()
                .product(product)
                .quantity(quantity)
                .build();
        return Optional.of(productQuantity);
    }
}