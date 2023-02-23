package pl.sii.shopsystem.order.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.customer.exception.CustomerException;
import pl.sii.shopsystem.order.dto.OrderInputDto;
import pl.sii.shopsystem.order.exception.OrderException;
import pl.sii.shopsystem.order.orderProduct.dto.OrderProductInputDto;
import pl.sii.shopsystem.order.service.OrderValidator;

import static pl.sii.shopsystem.customer.exception.CustomerExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static pl.sii.shopsystem.order.exception.OrderExceptionMessages.PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO;

@Component
public class OrderValidatorImpl implements OrderValidator {

    @Override
    public void validateOrderInputDto(OrderInputDto orderInputDto) {
        if (isCustomerIdBlank(orderInputDto)) {
            throw new CustomerException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
        for (OrderProductInputDto inputDto : orderInputDto.orderProducts()) {
            if (isAnyBlank(inputDto)) {
                throw new CustomerException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
            }
        }
    }

    @Override
    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new OrderException(PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO.getMessage() + quantity);
        }
    }

    private boolean isCustomerIdBlank(OrderInputDto orderInputDto) {
        return StringUtils.isAnyBlank(orderInputDto.customerId());
    }

    private boolean isAnyBlank(OrderProductInputDto inputDto) {
        return StringUtils.isAnyBlank(inputDto.productId(), inputDto.quantity());
    }
}
