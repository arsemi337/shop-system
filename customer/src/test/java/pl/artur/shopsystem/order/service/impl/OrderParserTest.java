package pl.artur.shopsystem.order.service.impl;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static exception.OrderExceptionMessages.PRODUCT_QUANTITY_IS_NOT_NUMBER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderParserTest {

    OrderParser underTest;

    public OrderParserTest() {
        this.underTest = new OrderParser();
    }

    @Test
    @DisplayName("when valid string quantity is passed, an int quantity is returned")
    void parseQuantity_should_returnIntQuantity_when_validStringQuantityIsPassed() {
        // given
        String quantity = "10";

        // when
        int parsedQuantity = underTest.parseQuantity(quantity);

        // then
        assertEquals(parsedQuantity, 10);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "\t", "\n", "\f", "a", "1d1", "fifty"})
    @NullAndEmptySource
    @DisplayName("when an invalid string quantity is passed, a NumberFormatException should be thrown")
    void parseQuantity_should_throwNumberFormatException_when_invalidStringQuantityIsPassed(String quantity) {
        // then
        assertThatExceptionOfType(NumberFormatException.class)
                .isThrownBy(() -> underTest.parseQuantity(quantity)).withMessage(PRODUCT_QUANTITY_IS_NOT_NUMBER.getMessage() + quantity);
    }
}
