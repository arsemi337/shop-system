package pl.artur.shopsystem.order.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.artur.shopsystem.order.orderProduct.dto.OrderProductInputDto;
import pl.artur.shopsystem.order.dto.OrderInputDto;
import pl.artur.shopsystem.order.service.OrderValidator;

import static exception.CustomerExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static exception.OrderExceptionMessages.PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO;

@Component
public class OrderValidatorImpl implements OrderValidator {

    @Override
    public void validateOrderInputDto(OrderInputDto orderInputDto) {
        if (isCustomerIdBlank(orderInputDto)) {
            throw new IllegalArgumentException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
        for (OrderProductInputDto inputDto : orderInputDto.orderProducts()) {
            if (isAnyBlank(inputDto)) {
                throw new IllegalArgumentException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
            }
        }
    }

    @Override
    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO.getMessage() + quantity);
        }
    }

    private boolean isCustomerIdBlank(OrderInputDto orderInputDto) {
        return StringUtils.isAnyBlank(orderInputDto.customerId());
    }

    private boolean isAnyBlank(OrderProductInputDto inputDto) {
        return StringUtils.isAnyBlank(inputDto.productName(), inputDto.quantity());
    }
}
