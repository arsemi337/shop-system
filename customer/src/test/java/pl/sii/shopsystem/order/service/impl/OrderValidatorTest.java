package pl.sii.shopsystem.order.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sii.shopsystem.order.dto.OrderInputDto;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductInputDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static exception.CustomerExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static exception.OrderExceptionMessages.PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {

    @InjectMocks
    OrderValidatorImpl underTest;

    @ParameterizedTest
    @ValueSource(strings = {" ", "\t", "\n", "\f"})
    @NullAndEmptySource
    @DisplayName("when an order input with blank fields is passed, an IllegalArgumentException should be thrown")
    void validateOrderInputDto_should_throwIllegalArgumentException_when_orderInputWithBlankCustomerIdIsPassed(String customerId) {
        // given
        OrderInputDto input = OrderInputDto.builder()
                .customerId(customerId)
                .build();

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateOrderInputDto(input)).withMessage(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "\t", "\n", "\f"})
    @NullAndEmptySource
    @DisplayName("when an order input with blank order product fields is passed, an IllegalArgumentException should be thrown")
    void validateOrderInputDto_should_throwIllegalArgumentException_when_orderInputWithBlankOrderProductFieldsIsPassed(String productId) {
        // given
        List<OrderProductInputDto> orderProducts = new ArrayList<>();
        orderProducts.add(OrderProductInputDto.builder()
                .productId(productId)
                .quantity("10")
                .build());
        OrderInputDto input = OrderInputDto.builder()
                .customerId(UUID.randomUUID().toString())
                .orderProducts(orderProducts)
                .build();

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateOrderInputDto(input)).withMessage(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    @DisplayName("when a quantity lower than one is passed, an IllegalArgumentException should be thrown")
    void validateQuantity_should_throwIllegalArgumentException_when_quantityLowerThanOneIsPassed(int quantity) {
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> underTest.validateQuantity(quantity)).withMessage(PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO.getMessage() + quantity);
    }
}