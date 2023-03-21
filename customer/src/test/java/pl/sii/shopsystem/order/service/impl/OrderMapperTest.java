package pl.sii.shopsystem.order.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sii.shopsystem.customer.model.Customer;
import pl.sii.shopsystem.order.dto.OrderOutputDto;
import pl.sii.shopsystem.order.model.Order;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductOutputDto;
import pl.sii.shopsystem.order.orderProduct.model.OrderProduct;
import pl.sii.shopsystem.product.model.Product;
import product.model.Genre;
import supplier.TimeSupplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

    @InjectMocks
    OrderMapper underTest;
    @Mock
    TimeSupplier timeSupplier;

    @Test
    @DisplayName("when correct data is passed, an OrderOutputDto object should be returned")
    void mapToOrderOutputDto_should_mapToOrderOutputDto_when_correctDataIsPassed() {
        // given
        Customer customer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        List<OrderProductOutputDto> purchasedProducts = new ArrayList<>();
        purchasedProducts.add(OrderProductOutputDto.builder()
                .title("Potop")
                .type(Genre.HISTORICAL_FICTION)
                .manufacturer("Greg")
                .price(new BigDecimal("9.99"))
                .quantity(1)
                .build());
        BigDecimal totalCost = new BigDecimal("9.99");

        // when
        OrderOutputDto orderOutputDto = underTest.mapToOrderOutputDto(customer, purchasedProducts, totalCost);

        // then
        assertEquals(orderOutputDto.customerFirstname(), customer.getFirstname());
        assertEquals(orderOutputDto.customerLastname(), customer.getLastname());
        assertEquals(orderOutputDto.customerEmail(), customer.getEmail());
        assertEquals(orderOutputDto.orderedProducts(), purchasedProducts);
        assertEquals(orderOutputDto.totalCost(), totalCost);
    }

    @Test
    @DisplayName("when correct data is passed, an OrderProducts list should be returned")
    void mapToOrderProducts_should_mapToOrderProducts_when_correctDataIsPassed() {
        // given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2010, 1, 1, 1, 10, 30));
        UUID uuid = UUID.randomUUID();
        Product product = Product.builder()
                .id(uuid)
                .creationTime(timeSupplier.get())
                .title("Potop")
                .type(Genre.HISTORICAL_FICTION)
                .manufacturer("Greg")
                .price(new BigDecimal("9.99"))
                .build();
        List<ProductQuantity> productQuantities = new ArrayList<>();
        productQuantities.add(ProductQuantity.builder()
                .product(product)
                .quantity(1)
                .build());
        Customer customer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        Order order = Order.builder()
                .customer(customer)
                .dateTime(timeSupplier.get())
                .cost(new BigDecimal("479.99"))
                .build();

        // when
        List<OrderProduct> orderProducts = underTest.mapToOrderProducts(productQuantities, order);

        // then
        assertEquals(orderProducts.get(0).getProduct(), product);
        assertEquals(orderProducts.get(0).getOrder(), order);
        assertEquals(orderProducts.get(0).getQuantity(), 1);
    }

    @Test
    @DisplayName("when productQuantities are passed, an OrderProductOutputDto list should be returned")
    void mapToOrderProductOutputDtoList_should_mapToOrderProductOutputDtoList_when_productQuantitiesArePassed() {
        // given
        when(timeSupplier.get()).thenReturn(LocalDateTime.of(2010, 1, 1, 1, 10, 30));
        UUID uuid = UUID.randomUUID();
        Product product = Product.builder()
                .id(uuid)
                .creationTime(timeSupplier.get())
                .title("Potop")
                .type(Genre.HISTORICAL_FICTION)
                .manufacturer("Greg")
                .price(new BigDecimal("9.99"))
                .build();
        List<ProductQuantity> productQuantities = new ArrayList<>();
        productQuantities.add(ProductQuantity.builder()
                .product(product)
                .quantity(1)
                .build());

        // when
        List<OrderProductOutputDto> orderProductOutputDtoList = underTest.mapToOrderProductOutputDtoList(productQuantities);

        // then
        assertEquals(orderProductOutputDtoList.get(0).title(), product.getTitle());
        assertEquals(orderProductOutputDtoList.get(0).type(), product.getType());
        assertEquals(orderProductOutputDtoList.get(0).manufacturer(), product.getManufacturer());
        assertEquals(orderProductOutputDtoList.get(0).price(), product.getPrice());
        assertEquals(orderProductOutputDtoList.get(0).quantity(), 1);
    }

    @Test
    @DisplayName("when correct data is passed, an Order object should be returned")
    void mapToOrder_should_mapToOrder_when_correctDataIsPassed() {
        // given
        Customer customer = Customer.builder()
                .firstname("John")
                .lastname("Doe")
                .email("jdoe@sii.pl")
                .build();
        BigDecimal totalCost = new BigDecimal("479.99");

        // when
        Order order = underTest.mapToOrder(customer, totalCost);

        // then
        assertEquals(order.getCustomer(), customer);
        assertEquals(order.getCost(), totalCost);
        assertEquals(order.getDateTime(), timeSupplier.get());
    }
}