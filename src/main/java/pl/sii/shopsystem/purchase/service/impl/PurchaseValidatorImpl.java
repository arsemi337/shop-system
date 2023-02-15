package pl.sii.shopsystem.purchase.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pl.sii.shopsystem.client.exception.ClientException;
import pl.sii.shopsystem.purchase.dto.PurchaseInputDto;
import pl.sii.shopsystem.purchase.exception.PurchaseException;
import pl.sii.shopsystem.purchase.purchaseProduct.dto.PurchaseProductInputDto;
import pl.sii.shopsystem.purchase.service.PurchaseValidator;

import static pl.sii.shopsystem.client.exception.ClientExceptionMessages.INPUT_DATA_CONTAINS_BLANK_FIELDS;
import static pl.sii.shopsystem.purchase.exception.PurchaseExceptionMessages.PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO;

@Component
public class PurchaseValidatorImpl implements PurchaseValidator {

    @Override
    public void validatePurchaseInputDto(PurchaseInputDto purchaseInputDto) {
        if (isClientIdBlank(purchaseInputDto)) {
            throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
        }
        for (PurchaseProductInputDto inputDto : purchaseInputDto.purchaseProducts()) {
            if (isAnyBlank(inputDto)) {
                throw new ClientException(INPUT_DATA_CONTAINS_BLANK_FIELDS.getMessage());
            }
        }
    }

    @Override
    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new PurchaseException(PRODUCT_QUANTITY_LOWER_OR_EQUAL_ZERO.getMessage() + quantity);
        }
    }

    private boolean isClientIdBlank(PurchaseInputDto purchaseInputDto) {
        return StringUtils.isAnyBlank(purchaseInputDto.clientId());
    }

    private boolean isAnyBlank(PurchaseProductInputDto inputDto) {
        return StringUtils.isAnyBlank(inputDto.productId(), inputDto.quantity());
    }
}
